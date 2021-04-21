(ns stronghand-3e-api.customers.order
  (:require [aero.core :refer (read-config)]
            [ring.util.http-response :refer :all]
            ;; [stronghand-3e-api.utils.writelog :as writelog]
            [stronghand-3e-api.utils.auth :as auth]
            [stronghand-3e-api.utils.conn :as conn]
            [stronghand-3e-api.db.sp-orders :as orders]))

(def env (read-config ".config.edn"))
(defn uuid [] (str (java.util.UUID/randomUUID)))
(def txid (atom ""))

(defn make-order
  [token] ;;issue-id others images locations appointment-at]
  (if (= (auth/authorized? token) true)
    ;; (reset! txid (java.util.UUID/randomUUID))
    (println "somthing working here")
    ;; (let [created-by (get (auth/token? token) :_id)]
    ;;   (try
    ;;     (reset! txid (java.util.UUID/randomUUID))
    ;;     (orders/orders conn/db {:ID @txid
    ;;                             :ISSUE_ID issue-id
    ;;                             :OTHERS others
    ;;                             :IMAGES images
    ;;                             :LOCATIONS locations
    ;;                             :TOTAL 5 ;;need to calculate get price from issuse
    ;;                             :APPOINTMENT_AT appointment-at
    ;;                             :CREATED_BY created-by})
    ;;     (catch Exception ex
    ;;     ;;   (writelog/op-log! (str "ERROR : FN make-order " (.getMessage ex)))
    ;;       {:error {:message "Internal server error"}}))
    ;;   )
    (unauthorized {:error {:message "Unauthorized operation not permitted"}}))
;; ID
;; :ISSUE_ID
;; :OTHERS
;; :IMAGES
;; :LOCATIONS
;; :TOTAL
;; :APPOINTMENT_AT
;; :CREATED_BY
  )