(ns stronghand-3e-api.ops.staff
  (:require [aero.core :refer (read-config)]
            [ring.util.http-response :refer :all]
            [stronghand-3e-api.utils.writelog :as writelog]
            [stronghand-3e-api.utils.auth :as auth]
            [stronghand-3e-api.utils.conn :as conn]
            [stronghand-3e-api.db.sp-users :as users]
            [stronghand-3e-api.db.sp-orders :as orders]
            [stronghand-3e-api.db.sp-user-level :as user-level]))

;; Managing Operation Staff
;; [x] Adding user to staff
;; [x] View all Staff
;; [x] View All staff by Dept
;; [x] Update Staff Information //-> user login and update info by themself
;; [x] Enable/disable Staff
(defn is-staff?
  [id]
  (if (not= (get (orders/get-users-by-id conn/db {:ID id}) :user_level_id)  (get (orders/get-user-level conn/db {:LEVEL_DEC "User"}) :id))
    true
    false))

(defn get-dept
  [token]
  (if (= (auth/authorized? token) true)
    (let [user_id (get (auth/token? token) :_id)]
      ;; If user is a staff
      (if (true? (is-staff? user_id))
        (try
          (ok (user-level/get-user-level conn/db))
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-dept " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

(defn get-all-user
  [token]
  (if (= (auth/authorized? token) true)
    (let [user_id (get (auth/token? token) :_id)]
      ;; If user is a staff
      (if (true? (is-staff? user_id))
        (try
          (ok (users/get-all-users-with-level conn/db))
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-all-user " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

(defn get-user-by-dept
  [token dept_id]
  (if (= (auth/authorized? token) true)
    (let [user_id (get (auth/token? token) :_id)]
      ;; If user is a staff
      (if (true? (is-staff? user_id))
        (try
          (ok (users/get-user-by-level conn/db {:USER_LEVEL_ID dept_id}))
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-all-user " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

(defn update-user-level
  [token staff_id dept_id]
  (if (= (auth/authorized? token) true)
    (let [user_id (get (auth/token? token) :_id)]
      ;; If user is a staff
      (if (true? (is-staff? user_id))
        (try
          (users/update-user-level conn/db {:ID staff_id :USER_LEVEL_ID dept_id})
          (ok {:message "User successfully updated"})
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-all-user " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

(defn update-user-status
  [token id status_id]
  (if (= (auth/authorized? token) true)
    (let [user_id (get (auth/token? token) :_id)]
      ;; If user is a staff
      (if (true? (is-staff? user_id))
        (try
          (users/update-user-status conn/db {:ID id :USER_STATUS status_id})
          (ok {:message "User status successfully updated"})
          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN get-all-user " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Unauthorized operation not permitted"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))