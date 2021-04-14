(ns stronghand-3e-api.utils.conn
  (:require [aero.core :refer (read-config)]))

(def env (read-config ".config.edn"))

(def db (get env :dburi))