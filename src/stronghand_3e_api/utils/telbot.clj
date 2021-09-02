(ns stronghand-3e-api.utils.telbot
    (:require   [aero.core :refer (read-config)]
                [clj-http.client :as client]))

(def env (read-config ".config.edn"))

(defn send-message
    [message]
    (client/get (get env :telegram_token)  {:query-params {:chat_id (get env :telegram_group_chat_id)
                                                                                                      :text message}
                                                                                                      :content-type :json
                                                                                                      :form-params false}))