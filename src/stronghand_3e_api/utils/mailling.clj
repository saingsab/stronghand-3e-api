(ns stronghand-3e-api.utils.mailling
  (:require [aero.core :refer (read-config)]
            [postal.core :refer [send-message]]
            [stronghand-3e-api.utils.writelog :as writelog]))

(def env (read-config ".config.edn"))

(defn send-mail! [mail-to mail-subject mail-content]
  (try
    (send-message {:host (str (get env :hostmail))
                   :port (Integer. (get env :portmail))
                   :ssl true
                   :user (str (get env :mailuser))
                   :pass (str (get env :mailpassword))}
                  {:from (str (get env :mailfrom))
                   :to mail-to
                   :subject mail-subject
                   :body [{:type "text/html"
                           :content mail-content}]})
    (catch Exception ex
      (writelog/op-log! (.getMessage ex))
      {:error {:message "Something went wrong on our end"}})))