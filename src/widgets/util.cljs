(ns widgets.util
  (:use util html))

(defn apply-style [el opts]
  (when (and opts (:style opts))
    (css el (:style opts))))


