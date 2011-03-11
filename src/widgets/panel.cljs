(ns widgets.panel
  (:use util html widgets.util))

(def wrap-css {:width "100%"
               :height "100%"
               :position :relative})

(def fill-css {:position :absolute
               :top 0
               :left 0
               :right 0
               :bottom 0})

(defn width [o]
  (if (has-el? o)
    (.width (:el o))
    (.width o)))

(defn height [o]
  (if (has-el? o)
    (.height (:el o))
    (.height o)))

(defn layout-css [name]
  (cond
   (= :fill name) fill-css
   :else {}))

(defn cover-left [panel content]
  (let [w (width panel)
        h (height panel)
        pc (panel-content content)
        children (.children (:el panel))]
    (css pc {:width w
             :height h
             :position :absolute
             :left (+ w 1)
             :top 0
             :zIndex 9999})
    (append (:el panel) pc)
    (.animate pc {:left 0}
              300
              (fn []
                (css children {:zIndex -100
                               :display :none})
                (css pc {:width "100%"
                         :height "100%"
                         :position :relative
                         :zIndex 0})))))

(defn back [panel]
  (let [w (width panel)
        h (height panel)
        children (.children (:el panel))
        l ($ (last children))
        bl ($ (nth children (dec (dec (count children)))))]
    (css bl {:display :block
             :zIndex -1})
    (css l {:position :absolute
            :top 0
            :left 0})
    (.animate l {:left w} 300
              (fn []
                (css bl {:width "100%"
                         :height "100%"
                         :position :relative
                         :zIndex 0})
                (.remove l)))))

(defn panel-content [content]
  (doto ($html [:div {:class "panel-content"}
                content])
    (apply-style opts)))

(defn panel [& o]
  (let [opts (if (string? (first o))
               (apply hash-map o)
               (first o)) 
        content (panel-content (:items opts))
        wrap ($html [:div {:class "panel"} content])
        layout (or (:layout opts) :fill)]
    (css wrap wrap-css)
    (when (:style opts)
      (css content (:style opts)))
    (css content (layout-css layout))
    {:el wrap
     :size (:size opts)}))



