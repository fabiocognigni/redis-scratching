(ns carmine-scratch.core
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def server1-conn {:pool {} :spec {}}) ; See `wcar` docstring for opts
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn foo
  "Basic commands."
  (wcar* (car/ping)
       (car/set "foo" "bar")
       (car/get "foo")))
