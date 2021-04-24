(ns stronghand-3e-api.usr-schema
  (:require   [compojure.api.sweet :refer [context POST GET resource]]
              [ring.util.http-response :refer [ok]]
              [ring.util.response :refer [redirect]]
              [schema.core :as s]
              [stronghand-3e-api.account.register :as register]
              [stronghand-3e-api.account.activation :as activation]
              [stronghand-3e-api.account.login :as login]))

;; (s/defschema Total
;;   {:total s/Int})

(def routes
  (context "/usr" []
    :tags ["usr"]

    (POST "/rigister-by-phone" []
      :summary "Provide phnone and password to register new user"
      :body-params [phone :- s/Str, password :- s/Str]
      (register/account-by-phone phone password))

    (POST "/resend-code" []
      :summary "Provide phone number to get new ativation code"
      :body-params [phone :- s/Str, password :- s/Str]
      (register/account-by-phone phone password))

    (POST "/login-by-phone" []
      :body-params [phone :- s/Str, password :- s/Str]
      :summary "Login with phone number get back token"
      (login/login-by-phone phone password))

    (POST "/account-confirmation" []
      :body-params [phone :- s/Str, verification-code :- s/Str]
      :summary "Confirm user account from phone"
      (if (= (activation/activate-user-by-phone phone verification-code) true)
        (ok {:message "User successfully activated"})
        (ok {:error {:message "User failed activation"}})))

    (POST "/rigister-by-mail" []
      :summary "Provide email and password to register new user"
      :body-params [email :- s/Str, password :- s/Str]
      (register/account-by-phone email password))

    (GET "/account-confirmation" []
      :query-params [userid :- s/Str, verification-code :- s/Str]
      :summary "Only show for email!"
      (if (= (activation/activate-user userid verification-code) true)
        (redirect "https://www.stronghand3e.com/successfullyverified.html")
        (redirect "https://www.stronghand3e.com/failedverification.html")))
      ;; (context "/ops" []
    ))