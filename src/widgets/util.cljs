(ns widgets.util
  (:use util html))

(def wrap-css {:width "100%"
               :height "100%"
               :position :relative})

(def fill-css {:position :absolute
               :top 0
               :left 0
               :right 0
               :bottom 0})

(defn apply-style [el opts]
  (when (and opts (:style opts))
    (css el (:style opts))))


