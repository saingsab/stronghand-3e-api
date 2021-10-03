(ns stronghand-3e-api.account.userinfor
  (:require  [ring.util.http-response :refer :all]
             [stronghand-3e-api.utils.auth :as auth]
             [stronghand-3e-api.utils.conn :as conn]
             [stronghand-3e-api.db.sp-status :as status]
             [stronghand-3e-api.utils.sms :as sms]
             [stronghand-3e-api.utils.genpin :as genpin]
             [stronghand-3e-api.utils.writelog :as writelog]
             [stronghand-3e-api.db.sp-users :as users]))

(def Status
  (get (status/get-status-by-name conn/db {:STATUS_NAME "active"}) :id))

(def pin-code (atom (genpin/getpin)))

(defn setup-profile!
  [token first_name mid_name last_name phonenumber gender image_uri address]
  (if (= (auth/authorized? token) true)
  ; letdo
    (try
      (users/setup-user-profile conn/db {:ID (get (auth/token? token) :_id)
                                         :FIRST_NAME first_name
                                         :MID_NAME mid_name
                                         :LAST_NAME last_name
                                         :PHONENUMBER phonenumber
                                         :GENDER gender
                                         :PROFILE_IMG image_uri
                                         :ADDRESS address
                                         :STATUS_ID Status})
      (ok {:message "Your profile have been saved successfully"})
      (catch Exception ex
        (writelog/op-log! (str "ERROR : " (.getMessage ex)))
        {:error {:message "Something went wrong on our end"}}))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

(defn get-user-profile
  [token]
  (if (= (auth/authorized? token) true)
    (try
      (ok (users/get-users-by-id conn/db {:ID (get (auth/token? token) :_id)}))
      (catch Exception ex
        (writelog/op-log! (str "ERROR : " (.getMessage ex)))
        (ok {:error {:message "Something went wrong on our end"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

    ; Verify valid phone first before add to db
(defn add-phone-number
  [token phone]
  (if (= (auth/authorized? token) true)
    (try
      (users/set-phonenumber-by-id conn/db {:ID (get (auth/token? token) :_id) :PHONENUMBER phone :TEMP_TOKEN @pin-code})
      (sms/send-sms (str "Your STRONGHAND 3E verification code is:" @pin-code) phone)
      (reset! pin-code (genpin/getpin))
      (ok {:message (str "We've sent you an SMS with the code to " phone)})
      (catch Exception ex
        (writelog/op-log! (str "ERROR : FN add-phone-number" (.getMessage ex)))
        (ok {:error {:message "Something went wrong on our end"}})))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))