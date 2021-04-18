(ns stronghand-3e-api.utils.sms
  (:require [aero.core :refer (read-config)]
            [clj-http.client :as client]
            [clojure.tools.logging :as log]))

(def env (read-config ".config.edn"))

(defn send-sms
  [body to]
  (try
    (client/post (get env :TWILIO_URL) {:headers {:Content-Type "application/x-www-form-urlencoded"
                                                  :Authorization (get env :U_KEY)}
                                        :form-params {:Body body
                                                      :To to
                                                      :From "STRONGHAND"}})
    (catch Exception ex
      (log/error ex))))

