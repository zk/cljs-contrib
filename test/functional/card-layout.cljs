(ns card-layout
  (:use util html)
  (:require [layout.card :as card]
            [layout.border :as border]
            [widgets :as wd]))


(ready
 (fn []
   (let [body ($ "body")
         card-layout (card/layout :items [[:div "Card 1"]
                                          [:div "Card 2"]])
         border-layout (border/layout :north ($html [:div "toolbar"])
                                      :center card-layout)]
     (wd/window-frame
      :content border-layout)
     ('setTimeout #(card/load card-layout ($html [:div {:style "background-color: blue;"} "Card 3"])) 1000))))
