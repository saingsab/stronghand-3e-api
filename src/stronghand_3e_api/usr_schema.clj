(ns stronghand-3e-api.usr-schema
  (:require   [compojure.api.sweet :refer [context POST resource]]
              [ring.util.http-response :refer [ok]]
              [schema.core :as s]
              [stronghand-3e-api.account.register :as register]
              [stronghand-3e-api.account.activation :as activation]))

(s/defschema Total
  {:total s/Int})

(def routes
  (context "/usr" []
    :tags ["usr"]

    (POST "/rigister-by-phone" []
      :summary "Provide phnone and password to register new user"
      :body-params [phone :- s/Str, password :- s/Str]
      (register/account-by-phone phone password)))

  (POST "/resend-code" []
    :summary "Provide phone number to get new ativation code"
    :body-params [email :- s/Str, password :- s/Str]
    (register/account-by-phone phone password))

  (POST "/account-confirmation" []
    :body [phone :- s/Str, verification_code :- s/Str]
    :summary "Confirm user account from phone"
    (if (= (activation/activate-user-by-phone phone verification_code) true)
      (ok {:message "User successfully activated"})
      (ok {:error {:message "User failed activation"}})))

  (POST "/rigister-by-mail" []
    :summary "Provide email and password to register new user"
    :body-params [email :- s/Str, password :- s/Str]
    (register/account-by-phone phone password))

  (GET "/account-confirmation" []
    :query-params [userid :- s/Str, verification-code :- s/Str]
    :summary "Only show for email!"
    (if (= (activation/activate-user userid verification-code) true)
      (redirect "https://www.stronghand3e.com/successfullyverified.html")
      (redirect "https://www.stronghand3e.com/failedverification.html")))

  ;; (context "/ops" []
  )