(ns stronghand-3e-api.usr-schema
  (:require   [compojure.api.sweet :refer [context POST GET resource]]
              [ring.util.http-response :refer [ok]]
              [ring.util.response :refer [redirect]]
              [schema.core :as s]
              [stronghand-3e-api.account.register :as register]
              [stronghand-3e-api.account.activation :as activation]
              [stronghand-3e-api.account.login :as login]
              [stronghand-3e-api.usr.order :as usr-orders]))

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
      :body-params [phone :- s/Str, verification_code :- s/Str]
      :summary "Confirm user account from phone"
      (if (= (activation/activate-user-by-phone phone verification_code) true)
        (ok {:message "User successfully activated"})
        (ok {:error {:message "User failed activation"}})))

    (POST "/forget-password-phone" []
      :summary "Input User phone number to received reseting code"
      :body-params [phone :- s/Str]
      (login/forget-password phone))

    (POST "/reset-password-by-phone" []
      :summary "Enter a valid reseting code from sms and set new password"
      :body-params [temp_code :- s/Str, phone :- s/Str, password :- s/Str]
      (login/reset-password! temp_code phone password))

    (POST "/rigister-by-mail" []
      :summary "Provide email and password to register new user"
      :body-params [email :- s/Str, password :- s/Str]
      (register/account-by-email email password))

    (GET "/account-confirmation" []
      :query-params [userid :- s/Str, verification-code :- s/Str]
      :summary "Only show for email!"
      (if (= (activation/activate-user userid verification-code) true)
        (redirect "https://www.stronghand3e.com/successfullyverified.html")
        (redirect "https://www.stronghand3e.com/failedverification.html")))

    (POST "/login-by-email" []
      :body-params [email :- s/Str, password :- s/Str]
      :summary "Login with email address get back token"
      (login/login-by-email email password))

    (POST "/forget-password-by-email" []
      :summary "Input User email address to received reseting code"
      :body-params [email :- s/Str]
      (login/forget-password-by-mail email))

    (POST "/reset-password-by-email" []
      :summary "Enter a valid reseting code and new password"
      :body-params [temp_code :- s/Str, email :- s/Str, password :- s/Str]
      (login/reset-password-by-mail! temp_code email password))

    (POST "/change-password" []
      :header-params [authorization :- s/Str]
      :summary "Enter current Password and new Password to change!"
      :body-params [current_password :- s/Str, new_password :- s/Str]
      (login/change-password! authorization current_password new_password))

    ;; Auth from external
    (POST "/login-from-google" []
      :body-params [token :- s/Str]
      :summary "Provide OAuth token return JWT token"
      (login/login-from-google token))

    (POST "/login-from-facebook" []
      :body-params [token :- s/Str]
      :summary "Provide OAuth token return JWT token"
      (login/login-from-facebook token))

    (POST "/order" []
      :summary "Client start order technicians"
      :header-params [authorization :- s/Str]
      :body-params [issue_id :- s/Str, others :- s/Str, images :- s/Str, locations :- s/Str, appointment_at :- s/Int]
      (usr-orders/make-order authorization
                             issue_id
                             others
                             images
                             locations
                             appointment_at))
    (GET "/list-order" []
      :summary "Get top list of order"
      :header-params [authorization :- s/Str]
      (usr-orders/get-recent-order authorization))

    (GET "/list-all-issues" []
      :summary "get all issues"
      :header-params [authorization :- s/Str]
      (usr-orders/list-all-issues authorization))

    (POST "/cancel-order" []
      :summary "Cancel order from user"
      :header-params [authorization :- s/Str]
      :body-params [order_id :- s/Str]
      (usr-orders/cancelled? authorization order_id))

    (POST "/list-order-by-status" []
      :summary "List the other by status"
      :header-params [authorization :- s/Str]
      :body-params [status_id :- s/Str]
      (usr-orders/get-order-by-status authorization status_id))

    (POST "/list-order-by-date" []
      :summary "List the order from date to date"
      :header-params [authorization :- s/Str]
      :body-params [from_date :- s/Str, to_date :- s/Str]
      (usr-orders/get-order-from-date-to-date authorization from_date to_date))
    ;; []
    ))