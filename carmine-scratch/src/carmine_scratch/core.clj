(ns carmine-scratch.core
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def server1-conn {:pool {} :spec {}})
;; See `wcar` docstring for opts
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn foo
  "Basic commands."
  (wcar* (car/ping)
       (car/set "foo" "bar")
       (car/get "foo")))

(defn set
  "Sets a key - value entry and the inverted entry for reverse lookup"
  [key val]
  (wcar* (car/set key val)
         (car/set val key)))

(defn get
  "Get the value by the key passed as param"
  [key]
  (wcar* (car/get key)))

(comment
  (set "key-1" "val-1")
  (get "key-1"))
