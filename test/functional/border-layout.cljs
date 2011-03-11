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
  (let [n0 (r255)
        n1 (r255)
        n2 (r255)
        bgcolor (str
                 "rgb("
                 (r255)
                 ","
                 (r255)
                 ","
                 (r255)
                 ")")
        fgcolor "black"]
    {:backgroundColor bgcolor
     :color fgcolor}))

(defn basic [center]
  (border/layout
   :north (panel :items "NORTH"
                 :size 50
                 :style (random-color))
   :center (or center
               (panel
                :items "CENTER"
                :style (random-color)))
   :west (panel
          :items "WEST"
          :style (random-color))
   :east (panel
          :items "EAST"
          :style (random-color))
   :south (panel
           :items "SOUTH"
           :style (random-color))))

(defn nested []
  (basic (basic (basic))))

(defn button [text f]
  (doto ($html [:a {:href "#"} text])
    (.click (fn []
              (f)
              false))))

(defn sizes []
  (border/layout
   :north (panel :items "NORTH 50"
                 :size 50
                 :style (random-color))
   :center (panel
            :items "CENTER"
            :style (random-color))
   :west (panel
          :items "WEST 100"
          :style (random-color)
          :size 100)
   :east (panel
          :items "EAST 200"
          :style (random-color)
          :size 200)
   :south (panel
           :items "SOUTH 25"
           :style (random-color)
           :size 25)))

(defn toolbar []
  ($html
   [:div {:class "toolbar"
          :style "background-color: white; border-bottom: solid black 1px;"}
    (button "basic" (fn []
                      (let [body ($ "body")]
                        (empty body)
                        (window-frame :content (main (basic))))))
    "&nbsp;"
    (button "nested" (fn []
                       (let [body ($ "body")]
                         (empty body)
                         (window-frame :content (main (nested))))))
    "&nbsp;"
    (button "sizes" (fn []
                       (let [body ($ "body")]
                         (empty body)
                         (window-frame :content (main (sizes))))))]))

(defn main [content]
  (border/layout :north (toolbar)
                 :center content))

(ready
 (fn []
   (window-frame
    :style {:backgroundColor "green"}
    :content (main (basic)))))




