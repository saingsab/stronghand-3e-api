(ns stronghand-3e-api.ops.order
  (:require [aero.core :refer (read-config)]
            [ring.util.http-response :refer :all]
            [stronghand-3e-api.utils.writelog :as writelog]
            [clojure.string :as str]
            [stronghand-3e-api.utils.auth :as auth]
            [stronghand-3e-api.utils.conn :as conn]
            [stronghand-3e-api.db.sp-orders :as orders]
            [stronghand-3e-api.db.sp-issues :as issues]
            [stronghand-3e-api.db.sp-rates :as rates]))

(def env (read-config ".config.edn"))
(defn uuid [] (str (java.util.UUID/randomUUID)))
(def txid (atom ""))

(defn is-staff?
  [id]
  (if (not= (get (orders/get-users-by-id conn/db {:ID id}) :user_level_id)  (get (orders/get-user-level conn/db {:LEVEL_DEC "User"}) :id))
    true
    false))

;; List order status Id
(defn list-order-status
  [token]
  (if (= (auth/authorized? token) true)
    (try
      (ok (orders/get-all-order-status conn/db))
      (catch Exception ex
        (writelog/op-log! (str "ERROR : FN list-order-status  " (.getMessage ex)))
        {:error {:message "Internal server error"}}))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; May be enable back for ordering for user
;; (defn make-order
;;   [token issue-id others images locations appointment-at]
;;   (if (= (auth/authorized? token) true)
;;     (let [created-by (get (auth/token? token) :_id)]
;;       (try
;;         (reset! txid (java.util.UUID/randomUUID))
;;         (orders/orders conn/db {:ID @txid
;;                                 :ISSUE_ID issue-id
;;                                 :OTHERS others
;;                                 :IMAGES images
;;                                 :LOCATIONS locations
;;                                 :TOTAL (get (issues/get-issue-by-id conn/db issue-id) :price);;need to calculate get price from issuse
;;                                 :APPOINTMENT_AT appointment-at
;;                                 :CREATED_BY created-by})
;;         (ok {:message "Successfully Order"})
;;         (catch Exception ex
;;           (writelog/op-log! (str "ERROR : FN make-order " (.getMessage ex)))
;;           {:error {:message "Internal server error"}})))
;;     (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; Select top 10 of recent order by staff. 
(defn get-recent-order
  [token]
  (if (= (auth/authorized? token) true)
    (let [user_id (get (auth/token? token) :_id)]
      ;; If user is a staff
      (if (true? (is-staff? user_id))
        (try
          (ok (orders/op-get-order-top conn/db))
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-recent-order " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; Get specific order by ID
(defn get-order
  [token order-id]
  (if (= (auth/authorized? token) true)
    (try
      (ok (orders/get-order-by-id conn/db {:ID order-id}))
      (catch Exception ex
        (writelog/op-log! (str "ERROR : FN get-recent-order " (.getMessage ex)))
        {:error {:message "Internal server error"}}))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; Order Cancalled
;; Condition Only Staff or Owner and in the placing order only
(defn cancelled?
  [token order-id]
  (if (= (auth/authorized? token) true)
    (let [user-id (get (auth/token? token) :_id)]
      (if (or (true? (is-staff? user-id)) (= user-id (get (orders/get-order-by-id conn/db {:ID order-id}) :created_by)))
        (try
          (println (get (orders/get-order-status conn/db {:ORDER_STATUS_DEC "Cancelled"}) :id))
          (orders/update-orders conn/db {:ID order-id
                                         :SOLUTIONS nil
                                         :TOTAL nil
                                         :ORDER_STATUS (get (orders/get-order-status conn/db {:ORDER_STATUS_DEC "Cancelled"}) :id)
                                         :UPDATED_BY user-id})
          (ok {:message "Order status successfully cancelled"})
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN canncled? " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

  ;; Update order status by operation or tech team only
(defn update-order-status
  [token order-id solutions total status-id]
  (if (= (auth/authorized? token) true)
    (let [user-id (get (auth/token? token) :_id)]
      (if (true? (is-staff? user-id))
        (try
          (orders/update-orders conn/db {:ID order-id
                                         :SOLUTIONS solutions
                                         :TOTAL total
                                         :ORDER_STATUS status-id
                                         :UPDATED_BY user-id})
          (ok {:message "Order status successfully updated"})
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN update-order-status " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;;  View Order by status
(defn get-order-by-status
  [token status-id]
  (if (= (auth/authorized? token) true)
    (let [user-id (get (auth/token? token) :_id)]
      (if (true? (is-staff? user-id))
        (try
          (ok (orders/get-order-by-status conn/db {:ORDER_STATUS status-id}))
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-order-by-status " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;;  View Order from date to date
(defn get-order-from-date-to-date
  [token from-date to-date]
  (if (= (auth/authorized? token) true)
    (let [user-id (get (auth/token? token) :_id)]
      (if (true? (is-staff? user-id))
        (try
          (ok (orders/get-order-from-date-to-date conn/db {:FROM_DATE from-date :TO_DATE to-date}))
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-order-from-date-to-date " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; Assgin Technician
(defn assign-technicains
  [token order_id technicians order_status]
  (if (= (auth/authorized? token) true)
    (let [user-id (get (auth/token? token) :_id)]
      (if (true? (is-staff? user-id))
        (try
          (orders/assign-technician conn/db {:ID order_id
                                             :TECHNICIANS  (into-array (str/split technicians #" "))
                                             :ORDER_STATUS order_status
                                             :UPDATED_BY user-id})
          (ok {:message "Successfully assigned technicains"})
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-order-from-date-to-date " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; Rates technicians
(defn ret-technician
  [token rate-star rate-dec rate-to]
  (if (= (auth/authorized? token) true)
    (let [created-by (get (auth/token? token) :_id)]
      (try
        (reset! txid (java.util.UUID/randomUUID))
        (rates/set-rates conn/db {:ID @txid
                                  :RATE_STAR rate-star
                                  :RATE_DEC rate-dec
                                  :RATE_TO rate-to
                                  :CREATED_BY created-by})
        (ok {:message "Thank you for your rating"})
        (catch Exception ex
          (writelog/op-log! (str "ERROR : FN make-order " (.getMessage ex)))
          {:error {:message "Internal server error"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))