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

    (POST "/assign-technicains" []
      :summary "Assigned Technicain and status of the order"
      :header-params [authorization :- s/Str]
      :body-params [order_id :- s/Str, technicians :- s/Str, order_status :- s/Str]
      (ops-orders/assign-technicains authorization order_id technicians order_status))
    ;; Enter new line
    ))