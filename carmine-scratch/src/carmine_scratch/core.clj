(ns carmine-scratch.core
  (:require [taoensso.carmine :as car :refer (wcar)])
  (:gen-class
    :methods [#^{:static true} [get [String] Object]
              #^{:static true} [set [String Object] clojure.lang.PersistentVector]]))

(def server1-conn {:pool {} :spec {}})
;; See `wcar` docstring for opts
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn foo
  "Basic commands."
  [] ;; required when use aot
  (wcar* (car/ping)
       (car/set "foo" "bar")
       (car/get "foo")))

(defn set-entry
  "Sets a key - value entry and the inverted entry for reverse lookup"
  [key val]
  (wcar* (car/set key val)
         (car/set val key)))

(defn get-val
  "Get the value by the key passed as param"
  [key]
  (wcar* (car/get key)))

(defn -set
  "A Java-callable wrapper around the 'set' function."
  [key val]
  (set-entry key val))

(defn -get
  "A Java-callable wrapper around the 'get' function."
  [key]
  (get-val key))


(comment
  (set-entry "key-1" "val-1")
  (get-val "key-1"))
