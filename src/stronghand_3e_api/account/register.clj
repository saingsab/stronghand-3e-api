(ns stronghand-3e-api.account.register
  (:require [aero.core :refer (read-config)]
            [ring.util.http-response :refer :all]
            [buddy.hashers :as hashers]
            [stronghand-3e-api.utils.validate :as validate]
            [stronghand-3e-api.utils.mailling :as mailling]
            [stronghand-3e-api.utils.writelog :as writelog]
            [stronghand-3e-api.utils.genpin :as genpin]
            [stronghand-3e-api.utils.sms :as sms]
            [clojure.tools.logging :as log]
            [stronghand-3e-api.utils.conn :as conn]
            [stronghand-3e-api.db.sp-status :as status]
            [stronghand-3e-api.db.sp-users :as users]))

(def env (read-config ".config.edn"))
(defn uuid [] (str (java.util.UUID/randomUUID)))

(def status-id
  (str (get (status/get-status-by-name conn/db {:STATUS_NAME "inactive"}) :id)))

(def user-id (atom (uuid)))
(def temp-token (atom (uuid)))
(def pin-code (atom (genpin/getpin)))

(defn phone-not-exist?
  [phone]
  (nil? (users/get-users-by-phone conn/db {:PHONENUMBER phone})))

(defn get-phone-by-id
  [id]
  (get (users/get-all-users-by-id conn/db {:ID id}) :phonenumber))

(defn email-not-exist?
  [email]
  (nil? (users/get-users-by-mail conn/db {:EMAIL email})))

(defn account-by-email
  [email password]
  (if (= (validate/email? email) true)
    ; True Save it to data base
    (if (= (email-not-exist? email) true)
      (try
       ; SAVE to Database and send welcome message
        (users/register-users-by-mail conn/db {:ID  @user-id  :EMAIL email :PASSWORD (hashers/derive password) :TEMP_TOKEN @temp-token :STATUS_ID status-id})
        ; Send email function here
        ;; (mailling/send-mail! email
        ;;                      "Activation Required"
        ;;                      (str "Welcome to Selendra! <br/> <br/> To complete verification please click the link below <br/> <br/><a href='https://" (str (get env :baseapi)) ".selendra.com/pub/v1/account-confirmation?userid=" @user-id "&verification-code=" @temp-token "' style='padding:10px 28px;background:#0072BC;color:#fff;text-decoration:none' target='_blank' data-saferedirecturl='https://api.selendra.com/pub/v1/account-confirmation?userid=" @user-id "&verification-code=" @temp-token "' >Verify Email</a> <br/> <br/> Best regards, <br/> Zeetomic Team <br/> https://selendra.com"))
        (reset! user-id (uuid))
        (reset! temp-token (uuid))
        (ok {:message (str "We've sent a message to " email " with a link to activate your account.")})
        (catch Exception ex
          (log/error ex)))
      (ok {:message "Your email account already exists!"}))
    ; Fale
    (ok {:message "Your email doesn't seem right!"})))

(defn account-by-phone
  [phone password]
  (if (= (validate/phone? phone) true)
  ; True
    (if (= (phone-not-exist? phone) true)
      (try
        ; SAVE to Database and send welcome message
        (users/register-users-by-phone conn/db {:ID (java.util.UUID/randomUUID) :PHONENUMBER phone :PASSWORD (hashers/derive password) :TEMP_TOKEN @pin-code :STATUS_ID status-id})
        (sms/send-sms (str "Your STRONGHAND 3E verification code is:" @pin-code) phone)
        (reset! pin-code (genpin/getpin))
        (ok {:message "Successfully registered!"})
        (catch Exception ex
          (log/error ex)))
      (ok {:message "Your phone number already exists!"}))
  ; Fale
    (ok {:message "Your phone number doesn't seem right!"})))

;;   ; Account from OAuth ID token


;; (defn invite-phone-number
;;   [token phone]
;;   (if (= (auth/authorized? token) true)
;;     (try
;;       (client/post (str (get env :smsendpoint)) {:form-params {:smscontent (str (get-phone-by-id (get (auth/token? token) :_id)) "invited you to join ZEETOMIC https://wallet.zeetomic.com") :phonenumber phone} :content-type :json})
;;       (ok {:message "Successfully invited!"})
;;       (catch Exception ex
;;         (writelog/op-log! (str "ERROR : FN invite-phone-number " (.getMessage ex)))
;;         {:error {:message "Something went wrong on our end"}}))
;;     (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

(defn resend-code
  [phone]
  (try
    (users/update-temp conn/db {:TEMP_TOKEN @pin-code :PHONENUMBER phone})
    (sms/send-sms (str "Your STRONGHAND 3E verification code is:" @pin-code) phone)
    (reset! pin-code (genpin/getpin))
    (ok {:message (str "We've sent you an SMS with the code to " phone)})
    (catch Exception ex
      (writelog/op-log! (str "ERROR : FN resend-code " (.getMessage ex)))
      {:error {:message "Something went wrong on our end"}})))