(ns stronghand-3e-api.account.login
  (:require [aero.core :refer (read-config)]
            [clojure.data.json :as json]
            [stronghand-3e-api.utils.auth :as auth]
            [stronghand-3e-api.utils.conn :as conn]
            [clj-http.client :as client]
            [stronghand-3e-api.db.sp-status :as status]
            [stronghand-3e-api.db.sp-users :as users]
            [buddy.hashers :as hashers]
            [buddy.sign.jwt :as jwt]
            [stronghand-3e-api.utils.validate :as validate]
            [stronghand-3e-api.utils.genpin :as genpin]
            [clj-time.core :as time]
            [stronghand-3e-api.utils.mailling :as mailling]
            [ring.util.http-response :refer :all]
            [stronghand-3e-api.utils.sms :as sms]
            [stronghand-3e-api.utils.writelog :as writelog]))

(def env (read-config ".config.edn"))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(def user-id (atom (uuid)))

(def pin-code (atom (genpin/getpin)))

(def inactive-status-id
  (get (status/get-status-by-name conn/db {:STATUS_NAME "inactive"}) :id))

(def disabled-status-id
  (get (status/get-status-by-name conn/db {:STATUS_NAME "disabled"}) :id))

(def active-status-id
  (get (status/get-status-by-name conn/db {:STATUS_NAME "active"}) :id))

(defn phone-not-exist?
  [phone]
  (nil? (users/get-users-by-phone conn/db {:PHONENUMBER phone})))

(defn email-not-exist?
  [email]
  (nil? (users/get-users-by-mail conn/db {:EMAIL email})))

(defn id-by-email
  [email]
  (get (users/get-users-by-mail conn/db {:EMAIL email}) :id))

(defn id-by-phone
  [phone]
  (get (users/get-users-by-phone conn/db {:PHONENUMBER phone}) :id))

(defn tokens
    ; 1day to be expired
  [info]
  (jwt/sign {:_id info :exp (time/plus (time/now) (time/seconds 86400))} (get (read-config ".config.edn") :JWTSEC)))

(defn is-no-active-mail?
  [email]
  (or (= inactive-status-id (get (users/get-users-by-mail conn/db {:EMAIL email}) :status_id))
      (= disabled-status-id (get (users/get-users-by-mail conn/db {:EMAIL email}) :status_id))))

(defn is-no-active-phone?
  [phone]
  (or (= inactive-status-id (get (users/get-users-by-phone conn/db {:PHONENUMBER phone}) :status_id))
      (= disabled-status-id (get (users/get-users-by-phone conn/db {:PHONENUMBER phone}) :status_id))))

(defn login-by-email
  [email password]
  (if (= (validate/email? email) true)
    (if (= (email-not-exist? email) true)
      (ok {:message "Your email address does not exist!"})
    ; If the user status id is inactive or disabled
      (if (= (is-no-active-mail? email) false)
        (if (= true (hashers/check password (get (users/get-users-by-mail conn/db {:EMAIL email}) :password)))
          (ok {:token (tokens (id-by-email email))})
          (ok {:error {:message "Login failed the username or password is incorrect"}}))
        (ok {:error {:message "Sorry, your user are not active"}})))
    (ok {:message "Your email doesn't seem right!"})))

(defn login-by-phone
  [phone password]
  (if (= (validate/phone? phone) true)
    (if (= (phone-not-exist? phone) true)
      (ok {:message "Your phone number does not exist!"})
      ; If the user status id is inactive or disabled
      (if (= (is-no-active-phone? phone) false)
        (if (= true (hashers/check password (get (users/get-users-by-phone conn/db {:PHONENUMBER phone}) :password)))
          (try
            (ok {:token (tokens (id-by-phone phone))})
            (catch Exception ex
              (writelog/op-log! (str "ERROR : " (.getMessage ex)))
              (ok {:error {:message "Something went wrong on our end"}})))
          (ok {:error {:message "Login failed the username or password is incorrect"}}))
        (ok {:error {:message "Sorry, your user login and password are not active"}})))
    (ok {:message "Your phone number doesn't seem right!"})))

; Auth from OAuth ID token from facebook
(defn post-req-facbook
  [access-token]
  (try
    (json/read-str
    ; Googleapis V1
     (get (client/post (str "https://identitytoolkit.googleapis.com/v1/accounts:signInWithIdp?key=" (get env :firebase))
                       {:form-params {:postBody (str "access_token=" access-token "&providerId=facebook.com")
                                      :requestUri "http://localhost"
                                      :returnIdpCredential true
                                      :returnSecureToken true}
                        :content-type :json}) :body) :key-fn keyword)
    (catch Exception ex
      (writelog/op-log! (str "ERROR : " (.getMessage ex)))
      "Internal server error")))

; Auth from OAuth ID token from Google
(defn post-req-google
  [id-token]
  (try
    (json/read-str
    ; Googleapis V1
     (get (client/post (str "https://identitytoolkit.googleapis.com/v1/accounts:signInWithIdp?key=" (get env :firebase))
                       {:form-params {:postBody (str "id_token=" id-token "&providerId=google.com")
                                      :requestUri "http://localhost"
                                      :returnIdpCredential true
                                      :returnSecureToken true}
                        :content-type :json}) :body) :key-fn keyword)
    (catch Exception ex
      (writelog/op-log! (str "ERROR : " (.getMessage ex)))
      "Internal server error")))

(defn login-from-facebook
  [token]
    ; If user email not exist in db then write to db and generate JWT
    ; If invalid token from Oauth will return msg
  (if (nil? (get (post-req-facbook token) :email))
    (ok {:error {:message "Opp!, token is not valid"}})
    (if (= (email-not-exist? (get (post-req-facbook token) :email)) true)
      (try
        (println (str "EMAIL: " (get (post-req-facbook token) :email)))
        (users/register-users-by-mail conn/db {:ID  @user-id  :EMAIL (get (post-req-facbook token) :email) :PASSWORD "******" :TEMP_TOKEN "0x" :STATUS_ID active-status-id})
        (reset! user-id (uuid))
              ; Sign JWT Token after write to db
        (ok {:token (tokens (id-by-email (get (post-req-facbook token) :email)))})
        (catch Exception ex
          (writelog/op-log! (str "ERROR : " (.getMessage ex)))
          (ok {:error {:message "Something went wrong on our end"}})))
            ; Exisitng email just return the JWT
      (ok {:token (tokens (id-by-email (get (post-req-facbook token) :email)))}))))

(defn login-from-google
  [token]
; If user email not exist in db then write to db and generate JWT
; If invalid token from Oauth will return msg
  (if (nil? (get (post-req-google token) :email))
    (ok {:error {:message "Opp!, token is not valid"}})
    (if (= (email-not-exist? (get (post-req-google token) :email)) true)
      (try
        (users/register-users-by-mail conn/db {:ID  @user-id  :EMAIL (get (post-req-google token) :email) :PASSWORD "******" :TEMP_TOKEN "0x" :STATUS_ID active-status-id})
        (reset! user-id (uuid))
        ; Sign JWT Token after write to db
        (ok {:token (tokens (id-by-email (get (post-req-google token) :email)))})
        (catch Exception ex
          (writelog/op-log! (str "ERROR : " (.getMessage ex)))
          (ok {:error {:message "Something went wrong on our end"}})))
      ; Exisitng email just return the JWT
      (ok {:token (tokens (id-by-email (get (post-req-google token) :email)))}))))

;; Forget password request by email
(defn forget-password-by-mail
  [email]
  (if (= (email-not-exist? email) true)
    (ok {:message "Your email does not exist!"})
    (try
    ; Create Temp code/ update to active status
      (users/update-temp-mail conn/db {:EMAIL email :TEMP_TOKEN @pin-code :STATUS_ID active-status-id})
   ; Sending temp code.
      (mailling/send-mail! email
                           "Resetting your STRONGHAND 3E password"
                           (str "Someone has asked to reset the password for your account.</br>If you did not request a password reset, you can disregard this email. No changes have been made to your account. <br/> <br/> 
                                 Below is your reset code:<br/> <br/>
                                 " @pin-code "<br/> <br/> Best regards, <br/> STRONGHAND 3E  Team <br/> https://stronghand3e.com"))

      (reset! pin-code (genpin/getpin))
      (ok {:message "We have sent you reset code please check your email"})
      (catch Exception ex
        (writelog/op-log! (str "ERROR : " (.getMessage ex)))))))

;; forget password request by phone
(defn forget-password
  [phone]
  (if (= (phone-not-exist? phone) true)
    (ok {:message "Your phone number does not exist!"})
    (try
    ; Create Temp code
      (users/update-temp conn/db {:PHONENUMBER phone :TEMP_TOKEN @pin-code})
   ; Sending temp code code.
   ;; (client/post (str (get env :smsendpoint)) {:form-params {:smscontent (str "Your Selendra reset code is:" @pin-code) :phonenumber phone} :content-type :json})
      (sms/send-sms (str "Your STRONGHAND 3E reset code is:" @pin-code) phone)
      (reset! pin-code (genpin/getpin))
      (ok {:message "We have sent you reset code please check your SMS!"})
      (catch Exception ex
        (writelog/op-log! (str "ERROR : " (.getMessage ex)))))))

;; Reset Password by email
(defn reset-password-by-mail!
  [temp-code email password]
  (if (= temp-code (get (users/get-users-by-mail conn/db {:EMAIL email}) :temp_token))
    (try
    ; match user and start reset new password
      (users/reset-password-by-mail conn/db {:EMAIL email :PASSWORD (hashers/derive password)})
      (ok {:message "Your password successfully updated!"})
      (catch Exception ex
        (writelog/op-log! (str "ERROR : " (.getMessage ex)))))
    (ok {:error {:message "Opps! your reset code was not correct!"}})))

(defn reset-password!
  [temp-code phone password]
  (if (= temp-code (get (users/get-users-by-phone conn/db {:PHONENUMBER phone}) :temp_token))
    (try
    ; match user and start reset new password
      (users/reset-password conn/db {:PHONENUMBER phone :PASSWORD (hashers/derive password)})
      (ok {:message "Your password successfully updated!"})
      (catch Exception ex
        (writelog/op-log! (str "ERROR : " (.getMessage ex)))))
    (ok {:error {:message "Opps! your reset code was not correct!"}})))

(defn change-password!
  [token current-password new-password]
  (if (= (auth/authorized? token) true)
    (if (hashers/check current-password (get (users/get-all-users-by-id conn/db {:ID (get (auth/token? token) :_id)}) :password))
      (try
        (users/change-password conn/db {:ID (get (auth/token? token) :_id) :PASSWORD (hashers/derive new-password)})
        (ok {:message "Your password successfully changed!"})
        (catch Exception ex
          (writelog/op-log! (str "ERROR : " (.getMessage ex)))))
      (ok {:error {:message "Opps! your current Password was not correct!"}}))
    (unauthorized {:error {:message "Unauthorized operation not permitted"}})))

; Auth from OAuth ID token from facebook
(defn post-req-facbook
  [access-token]
  (try
    (json/read-str
    ; Googleapis V1
     (get (client/post (str "https://identitytoolkit.googleapis.com/v1/accounts:signInWithIdp?key=" (get env :firebase))
                       {:form-params {:postBody (str "access_token=" access-token "&providerId=facebook.com")
                                      :requestUri "http://localhost"
                                      :returnIdpCredential true
                                      :returnSecureToken true}
                        :content-type :json}) :body) :key-fn keyword)
    (catch Exception ex
      (writelog/op-log! (str "ERROR : " (.getMessage ex)))
      "Internal server error")))

; Auth from OAuth ID token from Google
(defn post-req-google
  [id-token]
  (try
    (json/read-str
    ; Googleapis V1
     (get (client/post (str "https://identitytoolkit.googleapis.com/v1/accounts:signInWithIdp?key=" (get env :firebase))
                       {:form-params {:postBody (str "id_token=" id-token "&providerId=google.com")
                                      :requestUri "http://localhost"
                                      :returnIdpCredential true
                                      :returnSecureToken true}
                        :content-type :json}) :body) :key-fn keyword)
    (catch Exception ex
      (writelog/op-log! (str "ERROR : " (.getMessage ex)))
      "Internal server error")))

(defn login-from-facebook
  [token]
    ; If user email not exist in db then write to db and generate JWT
    ; If invalid token from Oauth will return msg
  (if (nil? (get (post-req-facbook token) :email))
    (ok {:error {:message "Opp!, token is not valid"}})
    (if (= (email-not-exist? (get (post-req-facbook token) :email)) true)
      (try
        (println (str "EMAIL: " (get (post-req-facbook token) :email)))
        (users/register-users-by-mail conn/db {:ID  @user-id  :EMAIL (get (post-req-facbook token) :email) :PASSWORD "******" :TEMP_TOKEN "0x" :STATUS_ID active-status-id})
        (reset! user-id (uuid))
              ; Sign JWT Token after write to db
        (ok {:token (tokens (id-by-email (get (post-req-facbook token) :email)))})
        (catch Exception ex
          (writelog/op-log! (str "ERROR : " (.getMessage ex)))
          (ok {:error {:message "Something went wrong on our end"}})))
            ; Exisitng email just return the JWT
      (ok {:token (tokens (id-by-email (get (post-req-facbook token) :email)))}))))

(defn login-from-google
  [token]
; If user email not exist in db then write to db and generate JWT
; If invalid token from Oauth will return msg
  (if (nil? (get (post-req-google token) :email))
    (ok {:error {:message "Opp!, token is not valid"}})
    (if (= (email-not-exist? (get (post-req-google token) :email)) true)
      (try
        (users/register-users-by-mail conn/db {:ID  @user-id  :EMAIL (get (post-req-google token) :email) :PASSWORD "******" :TEMP_TOKEN "0x" :STATUS_ID active-status-id})
        (reset! user-id (uuid))
        ; Sign JWT Token after write to db
        (ok {:token (tokens (id-by-email (get (post-req-google token) :email)))})
        (catch Exception ex
          (writelog/op-log! (str "ERROR : " (.getMessage ex)))
          (ok {:error {:message "Something went wrong on our end"}})))
      ; Exisitng email just return the JWT
      (ok {:token (tokens (id-by-email (get (post-req-google token) :email)))}))))