(ns stronghand-3e-api.ops.staff
  (:require [aero.core :refer (read-config)]
            [ring.util.http-response :refer :all]
            [stronghand-3e-api.utils.writelog :as writelog]
            [stronghand-3e-api.utils.auth :as auth]
            [stronghand-3e-api.utils.conn :as conn]
            [stronghand-3e-api.db.sp-users :as users]))

;; Managing Operation Staff
;; [ ] Adding user to staff
;; [ ] View all Staff
;; [ ] View All staff by Dept
;; [ ] View Staff by ID
;; [ ] Update Staff Information
;; [ ] Enable/disable Staff