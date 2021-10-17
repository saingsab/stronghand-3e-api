(ns stronghand-3e-api.usr-schema
  (:require   [compojure.api.sweet :refer [context POST GET resource]]
              [ring.util.http-response :refer [ok]]
              [ring.util.response :refer [redirect]]
              [schema.core :as s]
              [stronghand-3e-api.utils.writelog :as writelog]
              [stronghand-3e-api.utils.auth :as auth]
              [stronghand-3e-api.account.register :as register]
              [stronghand-3e-api.account.activation :as activation]
              [stronghand-3e-api.account.login :as login]
              [stronghand-3e-api.usr.order :as usr-orders]
              [stronghand-3e-api.account.userinfor :as userinfor]
              ;; Uploading File
              [compojure.api.upload :as upload]
              [clojure.contrib.duck-streams :as ds]
              ;; Operation Route
              [stronghand-3e-api.ops.order :as ops-orders]
              [stronghand-3e-api.ops.staff :as staff]))


(def routes
  (context "/usr" []
    :tags ["usr"]

    (POST "/rigister-by-phone" []
      :summary "Provide phnone and password to register new user"
      :body-params [phone :- s/Str, password :- s/Str]
      (register/account-by-phone phone password))

    (POST "/resend-code" []
      :summary "Provide phone number to get new ativation code"
      :body-params [phone :- s/Str]
      (register/resend-code phone ))

    (POST "/login-by-phone" []
      :body-params [phone :- s/Str, password :- s/Str]
      :summary "Login with phone number get back token"
      (login/login-by-phone phone password))

    (POST "/add-phonenumber"  []
      :header-params [authorization :- s/Str]
      :body-params [phone :- s/Str]
      :summary "Add phone number to existing user to verify"
      (userinfor/add-phone-number authorization phone))

    (POST "/account-confirmation" []
      :body-params [phone :- s/Str, verification_code :- s/Str]
      :summary "Confirm user account from phone"
      (if (= (activation/activate-user-by-phone phone verification_code) true)
        (ok {:message "User successfully activated"})
        (ok {:error {:message "User failed activation"}})))

    (POST "/account-confirmation" []
      :body-params [phone :- s/Str, verification_code :- s/Str]
      :summary "Confirm user account from phone"
      (if (= (activation/activate-user-by-phone phone verification_code) true)
        (ok {:message "User successfully activated"})
        (ok {:error {:message "User failed activation"}})))

    (POST "/forget-password-by-phone" []
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

    (POST "/login-from-apple" []
      :body-params [token :- s/Str]
      :summary "Login form aple ID"
      (login/login-from-apple token))

    (POST "/setup-profile" []
      :summary "Setting up user profile"
      :header-params [authorization :- s/Str]
      :body-params [first_name :- s/Str, mid_name :- s/Str, last_name :- s/Str, gender :- s/Str, image_uri :- s/Str, address :- s/Str]
      (userinfor/setup-profile! authorization
                                first_name
                                mid_name
                                last_name
                                gender
                                image_uri
                                address))
    (GET "/user-profile" []
      :summary "get user profile"
      :header-params [authorization :- s/Str]
      (userinfor/get-user-profile authorization))

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

    (GET "/list-top-order" []
      :summary "get all issues"
      :header-params [authorization :- s/Str]
      (usr-orders/get-recent-order authorization))

    (GET "/list-all-issues" []
      :summary "get all issues"
      :header-params [authorization :- s/Str]
      (usr-orders/list-all-issues authorization))

    (GET "/list-all-sub-categories" []
      :summary "get all sub categories"
      :header-params [authorization :- s/Str]
      (usr-orders/list-all-sub-categories authorization))

    (POST "/list-issues-by-categories" []
      :summary "List Issues by it's categories"
      :header-params [authorization :- s/Str]
      :body-params [id :- s/Str]
      (usr-orders/list-issues-by-categories authorization id))

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
      :body-params [from_date :- s/Inst, to_date :- s/Inst]
      (usr-orders/get-order-from-date-to-date authorization from_date to_date))

    (POST "/rate-technician" []
      :summary "Rate the technical team"
      :header-params [authorization :- s/Str]
      :body-params [rate_star :- s/Int, rate_dec :- s/Str, rate_to :- s/Str]
      (usr-orders/rate-technician authorization
                                  rate_star
                                  rate_dec
                                  rate_to))

    (GET "/list-order-status" []
      :summary "List the order status"
      :header-params [authorization :- s/Str]
      (usr-orders/list-order-status authorization))

    (POST "/upload" []
      :multipart-params [file :- upload/TempFileUpload]
      :middleware [upload/wrap-multipart-params]
      :header-params [authorization :- s/Str]
      (if (= (auth/authorized? authorization) true)
        (try
          (ds/copy (file :tempfile) (ds/file-str (str "img_db/" (get (dissoc file :tempfile) :filename))))
          (ok {:url (str "https://imagebank.stronghand3e.com/" (get (dissoc file :tempfile) :filename))})

          (catch Exception ex
            (writelog/op-log! (str "ERROR : FN Upload  " (.getMessage ex)))
            {:error {:message "Internal server error"}}))
        (ok {:error {:message "Something went wrong at our end"}})))
    ;;
    ;; opssc/routes
    ;; )
    (GET "/list-orders" []
      :summary "get all issues [OPS ->]"
      :header-params [authorization :- s/Str]
      (ops-orders/get-recent-order authorization))

    (POST "/get-order-by-status" []
      :summary "List the order by status id"
      :header-params [authorization :- s/Str]
      :body-params [status_id :- s/Str]
      (ops-orders/get-order-by-status authorization status_id))

    (POST "/get-order-from-date-to-date" []
      :summary "List the order by status id"
      :header-params [authorization :- s/Str]
      :body-params [from_date :- s/Inst, to_date :- s/Inst]
      (ops-orders/get-order-from-date-to-date authorization from_date to_date))

    (POST "/get-order-by-technicians" []
      :summary "List the order by status id"
      :header-params [authorization :- s/Str]
      :body-params [technician_id :- s/Str]
      (ops-orders/get-order-by-technicians authorization technician_id))

    (POST "/assign-technicians" []
      :summary "Assigned Technicain and status of the order"
      :header-params [authorization :- s/Str]
      :body-params [order_id :- s/Str, technicians :- s/Str, order_status :- s/Str]
      (ops-orders/assign-technicains authorization order_id technicians order_status))

    (POST "/get-technician-by-name" []
      :summary "Input first 3 latter of technicain name to get list of tech guys"
      :header-params [authorization :- s/Str]
      :body-params [name :- s/Str]
      (ops-orders/get-technician-by-name authorization name))

    (POST "/get-technician-by-email" []
      :summary "Input input of technicain email to get list of tech guys"
      :header-params [authorization :- s/Str]
      :body-params [email :- s/Str]
      (ops-orders/get-technician-by-email authorization email))

    (POST "/get-technician-by-phone" []
      :summary "Input phone number of technicain to get list of tech guys"
      :header-params [authorization :- s/Str]
      :body-params [phone :- s/Str]
      (ops-orders/get-technician-by-phone authorization phone))

    (POST "/update-order-status" []
      :summary "Adding solution and order status"
      :header-params [authorization :- s/Str]
      :body-params [order_id :- s/Str, solutions :- s/Str, total :- s/Num, status_id :- s/Str, appointment_at :- s/Int]
      (ops-orders/update-order-status authorization
                                      order_id
                                      solutions
                                      total
                                      status_id
                                      appointment_at))

    (GET "/get-dept" []
      :summary "List all departments"
      :header-params [authorization :- s/Str]
      (staff/get-dept authorization))

    (GET "/get-all-user" []
      :summary "List all departments"
      :header-params [authorization :- s/Str]
      (staff/get-all-user authorization))

    (POST "/get-user-by-dept" []
      :summary "Provide the dept id to get users"
      :header-params [authorization :- s/Str]
      :body-params [dept_id :- s/Str]
      (staff/get-user-by-dept authorization dept_id))

    (POST "/update-user-level" []
      :summary "Provission user to be a staff"
      :header-params [authorization :- s/Str]
      :body-params [staff_id :- s/Str, dept_id :- s/Str]
      (staff/update-user-level authorization staff_id dept_id))

    (POST "/update-user-status" []
      :summary "Provission user to be a staff"
      :header-params [authorization :- s/Str]
      :body-params [user_id :- s/Str, status_id :- s/Str]
      (staff/update-user-status authorization user_id status_id))))
;;
