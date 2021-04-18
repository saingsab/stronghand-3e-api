(ns stronghand-3e-api.account.activation
  (:require [stronghand-3e-api.utils.conn :as conn]
            [clojure.tools.logging :as log]
            [stronghand-3e-api.db.sp-users :as users]
            [stronghand-3e-api.db.sp-status :as status]))

(def status
  (get (status/get-status-by-name conn/db {:STATUS_NAME "active"}) :id))

(defn temp-tokens
  [id]
  (users/get-users-token conn/db {:ID id}))

(defn temp-tokens-by-phone
  [phone]
  (users/get-users-token-phone conn/db {:PHONENUMBER phone}))

(defn user-by-phone
  [phone]
  (users/get-users-by-phone conn/db {:PHONENUMBER phone}))

(defn activate-user
  [user-id temp-token]
  (if (= (get (temp-tokens user-id) :temp_token) temp-token)
    (try
      (users/user-activation conn/db {:ID user-id :TEMP_TOKEN "x" :STATUS_ID status})
      true
      (catch Exception ex
        (log/error ex)))
    false))

(defn activate-user-by-phone
  [phone temp-token]
  (if (= (str (get (temp-tokens-by-phone phone) :temp_token)) temp-token)
    (try
      (users/user-activation-by-phone conn/db {:PHONENUMBER phone :TEMP_TOKEN "0" :STATUS_ID status})
      true
      (catch Exception ex
        (println ex)))
    false))