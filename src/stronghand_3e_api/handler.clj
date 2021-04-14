(ns stronghand-3e-api.handler
  (:require [compojure.api.sweet :refer :all]
            ;;[ring.util.http-response :refer :all]
            [stronghand-3e-api.schema :as sc]))

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Stronghand-3e-api"
                   :description "Compojure Api example"}
            ;;:tags [{:name "api", :description "some apis"}]
            }}}
   sc/routes))
