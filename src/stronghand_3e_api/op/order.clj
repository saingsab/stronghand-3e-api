(ns stronghand-3e-api.op.order
  (:require [aero.core :refer (read-config)]
            [ring.util.http-response :refer :all]
            [stronghand-3e-api.utils.writelog :as writelog]
            [stronghand-3e-api.utils.auth :as auth]
            [stronghand-3e-api.utils.conn :as conn]
            [stronghand-3e-api.db.sp-orders :as orders]
            [stronghand-3e-api.db.sp-issues :as issues]))

(def env (read-config ".config.edn"))
(defn uuid [] (str (java.util.UUID/randomUUID)))
(def txid (atom ""))

(defn is-staff?
  [id]
  (if (not= id (get (orders/get-user-level conn/db {:LEVEL_DEC "User"}) :id))
    true
    false))

(defn make-order
  [token issue-id others images locations appointment-at]
  (if (= (auth/authorized? token) true)
    (let [created-by (get (auth/token? token) :_id)]
      (try
        (reset! txid (java.util.UUID/randomUUID))
        (orders/orders conn/db {:ID @txid
                                :ISSUE_ID issue-id
                                :OTHERS others
                                :IMAGES images
                                :LOCATIONS locations
                                :TOTAL (get (issues/get-issue-by-id conn/db issue-id) :price);;need to calculate get price from issuse
                                :APPOINTMENT_AT appointment-at
                                :CREATED_BY created-by})
        (ok {:message "Successfully Order"})
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
        (ok (orders/get-order-top conn/db {:CREATED_BY created-by}))
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

;; Update order status by operation or tech team only
;; (defn update-order-status
;;   [token status-id])