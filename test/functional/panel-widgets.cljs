(ns functional.panel-widgets
  (:use util html widgets widgets.panel))

(defn timeout [f delay]
  ('setTimeout f (if delay delay 1000)))

(defn run [p]
  (timeout #(cover p (panel :items "BAR"
                            :style {:backgroundColor "blue"
                                    :padding "20px"}))
           500)
  ('setTimeout #(cover p (panel :items "BAZ"
                                :style {:backgroundColor "red"
                                        :padding "20px"}))
               1000)
  ('setTimeout #(cover p (panel :items "BAP"
                                :style {:backgroundColor "black"
                                        :color "white"
                                        :padding "20px"}))
               1500)
  
  ('setTimeout #(back p) 2500)
  ('setTimeout #(back p) 3000)
  ('setTimeout #(back p) 3500))

(ready
 (fn []
   (let [p (panel :items [:div
                          "FOO"
                          [:br]
                          [:br]
                          (doto ($html [:a {:href "#"} "again"])
                            (css {:backgroundColor "white"
                                  :padding "5px"})
                            (.click (fn []
                                      (run p)
                                      false)))]
                  :style {:backgroundColor "green"
                          :padding "20px"})]
     (window-frame
      :content p)
     (run p))))
