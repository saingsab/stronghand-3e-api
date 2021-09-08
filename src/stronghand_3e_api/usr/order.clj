(ns stronghand-3e-api.usr.order
  (:require [aero.core :refer (read-config)]
            [ring.util.http-response :refer :all]
            [stronghand-3e-api.utils.writelog :as writelog]
            [clojure.string :as str]
            (clj-time [core :as time] [coerce :as tc])
            [stronghand-3e-api.utils.auth :as auth]
            [stronghand-3e-api.utils.conn :as conn]
            [stronghand-3e-api.db.sp-orders :as orders]
            [stronghand-3e-api.db.sp-issues :as issues]
            [stronghand-3e-api.db.sp-rates :as rates]
            [stronghand-3e-api.utils.telbot :as telbot]))

(def env (read-config ".config.edn"))
(defn uuid [] (str (java.util.UUID/randomUUID)))
(def txid (atom ""))

(defn is-user?
  [id]
  (if (not= id (get (orders/get-user-level conn/db {:LEVEL_DEC "User"}) :id))
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

;; List All issue
(defn list-all-issues
  [token]
  (if (= (auth/authorized? token) true)
    (try
      (ok (orders/get-all-issue conn/db))
      (catch Exception ex
        (writelog/op-log! (str "ERROR : FN list-all-issues  " (.getMessage ex)))
        {:error {:message "Internal server error"}}))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

(defn list-all-sub-categories
  [token]
  (if (= (auth/authorized? token) true)
    (try
      (ok (orders/get-all_categories conn/db))
      (catch Exception ex
        (writelog/op-log! (str "ERROR : FN list-all-issues  " (.getMessage ex)))
        {:error {:message "Internal server error"}}))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; List issue by it's catagory
(defn list-issues-by-categories
  [token id]
  (if (= (auth/authorized? token) true)
    (try
      (ok (orders/get-sub-category-id conn/db  {:ID id}))
      (catch Exception ex
        (writelog/op-log! (str "ERROR : FN list-issues-by-categories  " (.getMessage ex)))
        {:error {:message "Internal server error"}}))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

(defn make-order
  [token issue-id others images locations appointment-at]
  (if (= (auth/authorized? token) true)
    (let [created-by (get (auth/token? token) :_id)]
      (try
        (reset! txid (java.util.UUID/randomUUID))
        ;; Adding Order to db
        (orders/orders conn/db {:ID @txid
                                :ISSUE_ID issue-id
                                :OTHERS others
                                :IMAGES (into-array (str/split images #" "))
                                :LOCATIONS locations
                                :TOTAL (get (issues/get-issue-by-id conn/db issue-id) :price);;need to calculate get price from issuse
                                :APPOINTMENT_AT appointment-at
                                :CREATED_BY created-by})
        ;; Sending Notify Operation
        (telbot/send-message (str "New order\n "
                                  "ID: " @txid "\n"
                                  "Issue: " (get (orders/get-order-by-id conn/db {:ID (str @txid)}) :issues_name) "\n"
                                  "Locations: " (get (orders/get-order-by-id conn/db {:ID (str @txid)}) :locations) "\n"
                                  "Order by email: " (get (orders/get-users-by-id conn/db {:ID created-by}) :email) "\n"
                                  "Order by Phone: " (get (orders/get-users-by-id conn/db {:ID created-by}) :phonenumber) "\n"))
        (ok {:message "Successfully Ordered" :estimate_price (get (issues/get-issue-by-id conn/db issue-id) :price)})
        (catch Exception ex
          (writelog/op-log! (str "ERROR : FN make-order " (.getMessage ex)))
          {:error {:message "Internal server error"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; Select top 10 of recent order. 
(defn get-recent-order
  [token]
  (if (= (auth/authorized? token) true)
    (let [created-by (get (auth/token? token) :_id)]
      (try
        (ok {:message (orders/get-order-top conn/db {:CREATED_BY created-by})})
        (catch Exception ex
          (writelog/op-log! (str "ERROR : FN get-recent-order " (.getMessage ex)))
          {:error {:message "Internal server error"}})))
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
  [token order_id]
  (if (= (auth/authorized? token) true)
    (let [user_id (get (auth/token? token) :_id)]
      (if (or (true? (is-user? user_id)) (= user_id (get (orders/get-order-by-id conn/db {:ID user_id}) :created_by)))
        (try

          (orders/update-orders conn/db {:ID order_id
                                         :SOLUTIONS nil
                                         :TOTAL nil
                                         :ORDER_STATUS (get (orders/get-order-status conn/db {:ORDER_STATUS_DEC "Cancelled"}) :id)
                                         :UPDATED_BY user_id})
          (ok {:message "Order status successfully cancelled"})
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN canncled? " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

  ;; Update order status by operation or tech team only
(defn update-order-status
  [token order_id solutions total status_id]
  (if (= (auth/authorized? token) true)
    (let [user_id (get (auth/token? token) :_id)]
      (if (true? (is-user? user_id))
        (try
          (orders/update-orders conn/db {:ID order_id
                                         :SOLUTIONS solutions
                                         :TOTAL total
                                         :ORDER_STATUS status_id
                                         :UPDATED_BY user_id})
          (ok {:message "Order status successfully updated"})
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN update-order-status " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;;  View Order by status
(defn get-order-by-status
  [token status_id]
  (if (= (auth/authorized? token) true)
    (let [user-id (get (auth/token? token) :_id)]
      (if (true? (is-user? user-id))
        (try
          (ok (orders/get-order-by-status conn/db {:ORDER_STATUS status_id}))
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-order-by-status " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;;  View Order from date to date
(defn get-order-from-date-to-date
  [token from_date to_date]
  (if (= (auth/authorized? token) true)
    (let [user-id (get (auth/token? token) :_id)]
      (if (true? (is-user? user-id))
        (try
         ;; change from convert from string by accepting date from client.
          (ok (orders/get-order-from-date-to-date conn/db {:FROM_DATE (tc/to-sql-time  from_date)
                                                           :TO_DATE (tc/to-sql-time to_date)}))
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-order-from-date-to-date " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; Assgin Technician
(defn assign-technicains
  [token technicians order_status]
  (if (= (auth/authorized? token) true)
    (let [user-id (get (auth/token? token) :_id)]
      (if (true? (is-user? user-id))
        (try
          (orders/assign-technician conn/db {:TECHNICIANS technicians
                                             :ORDER_STATUS order_status
                                             :CREATED_BY user-id})
          (ok {:message "Successfully assigned technicains"})
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-order-from-date-to-date " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

;; Rates technicians
(defn rate-technician
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
          (writelog/op-log! (str "ERROR : FN rate-technician " (.getMessage ex)))
          {:error {:message "Internal server error"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))