(ns stronghand-3e-api.ops-schema
  (:require   [compojure.api.sweet :refer [context POST resource]]
              [ring.util.http-response :refer [ok]]
              [schema.core :as s]))

(s/defschema Total
  {:total s/Int})

(def routes
  (context "/ops" []
    :tags ["ops"]

    (POST "/plus" []
      :summary "plus with schema"
      :body-params [x :- s/Int, {y :- s/Int 0}]
      :return Total
      (ok {:total (+ x y)}))))