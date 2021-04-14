 (defproject stronghand-3e-api "0.1.0-SNAPSHOT"
   :description "FIXME: write description"
   :dependencies [[org.clojure/clojure "1.10.0"]
                  [metosin/compojure-api "2.0.0-alpha30"]
                  [metosin/ring-swagger-ui "3.20.1"]

                  ;Logging
                  [org.clojure/tools.logging "1.1.0"]]
   :ring {:handler stronghand-3e-api.handler/app}
   :uberjar-name "server.jar"
   :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                    :plugins [[lein-ring "0.12.5"]]}})
