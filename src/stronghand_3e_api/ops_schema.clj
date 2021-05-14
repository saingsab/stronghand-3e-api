(ns stronghand-3e-api.ops-schema
  (:require   [compojure.api.sweet :refer [context POST GET resource]]
              [ring.util.http-response :refer [ok]]
              [schema.core :as s]
              ;; [stronghand-3e-api.account.login :as login]
              [stronghand-3e-api.ops.order :as ops-orders]))

(def routes
  (context "/ops" []
    :tags ["ops"]

    (GET "/list-orders" []
      :summary "get all issues"
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
      :body-params [order_id :- s/Str, solutions :- s/Str, total :- s/Num, status_id :- s/Str]
      (ops-orders/get-technician-by-phone authorization
                                          order_id
                                          solutions
                                          total
                                          status_id))
    ;; Enter new line
    ))