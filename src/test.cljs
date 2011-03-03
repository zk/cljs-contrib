(ns test
  (:use util)
  (:import TestCase _ [jQuery :as $]))

(defn is [a]
  ('assertTrue a))

(defn _= [a b]
  (.isEqual _ a b))

(defn deftest [name & assertions]
  (let [tc (TestCase. name)
        counter 0]
    (map
     #(set! (aget tc.prototype (str "test-" (do (set! counter (inc counter))
                                                counter))) %)
     assertions)))

(defn empty-body []
  (.empty ($ "body")))

(defn in-dom [sel]
  (> (count ($ sel)) 0))

(defn append-body [& els]
  (append ($ "body") els))
