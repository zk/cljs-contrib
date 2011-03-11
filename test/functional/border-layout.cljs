(ns functional.border-layout
  (:use util html widgets)
  (:require [layout.border :as border]))

(defn toolbar [& o]
  (let [opts (apply hash-map o)
        left ($html [:div {:class "left"}
                     (:left opts)])
        center ($html [:div {:class "center"}
                       (:center opts)])
        right ($html [:div {:class "right"}
                      (:right opts)])]
    (css left {
               :width 200})
    (css right {:float "right"
                :width 200})
    (css center {:marginLeft "auto"
                 :marginRight "auto"
                 :textAlign "center"})
    (panel (merge opts {:items [left right center]}))))

(defn r255 []
  (.floor 'Math (* 255 (.random 'Math))))

(defn random-color []
  (str
   "rgb("
   (r255)
   ","
   (r255)
   ","
   (r255)
   ")"))

(defn gen-bl [center]
  (border/layout
   :north (panel :items "NORTH"
                 :size 50
                 :style {:backgroundColor (random-color)})
   :center (or center
               (panel
                :items "CENTER"
                :style {:backgroundColor (random-color)}))
   :west (panel
          :items "WEST"
          :style {:backgroundColor (random-color)})
   :east (panel
          :items "EAST"
          :style {:backgroundColor (random-color)})
   :south (panel
           :items "SOUTH"
           :style {:backgroundColor (random-color)
                   :color "white"})))

(ready
 (fn []
   (window-frame
    :style {:backgroundColor "green"}
    :content (gen-bl (gen-bl)))))

#_(window-frame
    :style {:backgroundColor "green"}
    :content (border/layout
              :north (panel :items "NORTH"
                            :style {:backgroundColor "blue"})
              :west (panel
                     :items "WEST"
                     :style {:backgroundColor "yellow"})
              :center (panel
                       :items "CENTER"
                       :style {:backgroundColor "cyan"})
              :east (panel
                     :items "EAST"
                     :style {:backgroundColor "pink"})
              :south (panel
                      :items "SOUTH"
                      :style {:backgroundColor "red"
                              :color "white"})))

#_(border-layout
   :north
   (panel :items [($html [:div "FOO!"])]
          :style {:backgroundColor "blue"}))

#_(toolbar :left (panel :items ($html [:button "stuff"]))
                              :center (panel :items ($html [:div "TITLE"]))
                              :right ($html [:button "cool stuff"]))





