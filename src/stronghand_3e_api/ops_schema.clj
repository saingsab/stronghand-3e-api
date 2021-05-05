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

    (POST "/assign-technicians" []
      :summary "Assigned Technicain and status of the order"
      :header-params [authorization :- s/Str]
      :body-params [order_id :- s/Str, technicians :- s/Str, order_status :- s/Str]
      (ops-orders/assign-technicains authorization order_id technicians order_status))

    (POST "/get-technician-by-name" []
      :summary "Input first 3 latter of technicain name to get list of tech guys"
      :header-params [authorization :- s/Str]
      :body-params [name :- s/Str]
      (ops-orders/get-technicain-by-name authorization name))

    (POST "/get-technician-by-email" []
      :summary "Input input of technicain email to get list of tech guys"
      :header-params [authorization :- s/Str]
      :body-params [email :- s/Str]
      (ops-orders/get-technicain-by-email authorization email))

    (POST "/get-technician-by-phone" []
      :summary "Input phone number of technicain to get list of tech guys"
      :header-params [authorization :- s/Str]
      :body-params [phone :- s/Str]
      (ops-orders/get-technicain-by-phone authorization phone))
    ;; Enter new line
    ))