 (defproject stronghand-3e-api "0.1.0-SNAPSHOT"
   :description "STRONGHAND 3E: CORE BACKEND"
   :dependencies [[org.clojure/clojure "1.10.0"]
                  [org.clojure/clojure-contrib "1.2.0"]
                  [metosin/compojure-api "2.0.0-alpha30"]
                  [metosin/ring-swagger-ui "3.20.1"]
                  ; ENV
                  [aero "1.1.3"]
                  ; DATABASE
                  [com.layerware/hugsql "0.5.1"]
                  [org.postgresql/postgresql "42.2.2"]
                  ; mail
                  [com.draines/postal "2.0.3"]
                  ; Hash
                  [buddy/buddy-hashers "1.4.0"]
                  [buddy/buddy-auth "2.2.0"]
                  ;; Http Client
                  [clj-http "3.12.0"]
                  ;Logging
                  [org.clojure/tools.logging "1.1.0"]
                  ;CORSE 
                  [ring-cors "0.1.13"]]
   :ring {:handler stronghand-3e-api.handler/app}
   :uberjar-name "server.jar"
   :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                    :plugins [[lein-ring "0.12.5"]]}})
