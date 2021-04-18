(ns stronghand-3e-api.utils.genpin
  (:import (java.util Random)))

(def r (new Random))
(defn random-numbers [max]
  (iterate (fn [ignored-arg] (.nextInt r max)) (.nextInt r max)))

(defn getpin
  []
  (apply str (take 6 (random-numbers 10))))
