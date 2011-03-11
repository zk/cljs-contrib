(ns layout.card
  (:use html util))

(def card-wrap-css {:backgroundColor "green"
                    :position "absolute"
                    :top 0
                    :right 0
                    :bottom 0
                    :left 0})

(def card-content-css {:position "relative"
                       :width "100%"
                       :height "100%"})

(defn load [cl content]
  (empty (:content cl))
  (append (:content cl) content))

(defn layout [& o]
  (let [opts (apply hash-map o)
        items (:items opts)
        content ($html [:div {:class "card-layout-content"}
                        (first items)])
        wrap ($html [:div {:class "card-layout-wrapper"}
                     content])]
    (css wrap card-wrap-css)
    (css content card-content-css)
    {:el wrap
     :content content
     :items items
     :layout (fn [])}))
