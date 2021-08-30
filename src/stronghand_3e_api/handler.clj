(ns stronghand-3e-api.handler
  (:require [compojure.api.sweet :refer :all]
            ;;[ring.util.http-response :refer :all]
            ;; [ring.middleware.cors :refer [wrap-cors]]
            [jumblerg.middleware.cors :refer [wrap-cors]]
            [stronghand-3e-api.usr-schema :as usrsc]
            ;; [stronghand-3e-api.ops-schema :as opssc]
            ))

;; (defn routers []
;;    usrsc/routes
;;    opssc/routes
;; )

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "STRONGHAND 3E API"
                   :description "Welcome to the STRONGHAND 3E API , This APIs are a set of endpoints created to manage customer order and matching in our platform."
                   :contact {:name "Officail Website"
                             :email "saing.sab@gmail.com"
                             :url "https://www.stronghand3e.com"}}}}}

;;   ;; accept everything
;; (wrap-cors routes #".*")
;; (wrap-cors routes identity)

;; ;; accept some things [todo on production]
;; (wrap-cors routes #".*localhost.*" #".*mydomain.org$")
;; (wrap-cors routes #(= (:allowed-origin db) %))

;; Accept Everthing during dev
(wrap-cors usrsc/routes #".*")
(wrap-cors usrsc/routes identity)
usrsc/routes
))