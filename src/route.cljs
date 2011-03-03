(ns route
  (:import RegExp $))

;; Inspired by Backbone.js' routing.

(def named-param (RegExp. ":([\\\\w\\\\d]+)" "g"))

(def splat-param (RegExp. "\\\\*([\\\\w\\\\d]+)" "g"))

(def escape-regexp (RegExp. "[-[\\]{}()+?.,\\\\\\\\^$|#\\\\s]" "g"))

(defn str-to-route [route-str]
  (let [inter-str (-> route-str
                      (.replace escape-regexp "\\\\\\\\$&")
                      (.replace named-param "([^\\\\/]*)")
                      (.replace splat-param "(.*?)"))]
    (RegExp. (str "^" inter-str "$"))))

(defn parse-params [route hash]
  (-> (:route route)
      (.exec)
      (.slice 1)))


(defn make-router []
  (let [routes []
        on-change (fn []
                    (let [match (.detect _ routes
                                         (fn [r]
                                           (.test (:route r) 'location.hash)))]
                      (if match
                        (do
                          (.apply (:callback match) 'this (parse-params match 'location.hash))))))]
    (.hashchange
     ($ 'window)
     on-change)
    {:route (fn [route-str callback]
              (let [route (str-to-route route-str)]
                (conj routes {:route route
                              :callback callback})))
     :run on-change}))


