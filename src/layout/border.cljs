(ns layout.border
  (:use html util))

(defn hide [bl tag]
  (when (aget bl (str tag "-el"))
    (let [blel (:el bl)
          panel (.find blel (str ".border-layout-" tag))
          center (.find blel ".border-layout-center")
          west (.find blel ".border-layout-west")
          panel-size (cond
                      (= :west tag) (.outerWidth panel)
                      (= :east tag) (.outerWidth panel)
                      (= :north tag) (.outerHeight panel)
                      (= :south tag) (.outerHeight panel))]
      (when (or (= :west tag) (= :east tag))
        (.animate panel {:width 0} 300)
        (.animate center {:marginLeft 0
                          :width (+ (.width center)
                                    panel-size)} 300))
      (when (or (= :north tag) (= :south tag))
        (.animate panel {:height 0} 300)
        (.animate center {:height (+ (.height center)
                                     panel-size)}
                  300)
        (.animate west {:height (+ (.height center)
                                     panel-size)}
                  300)))))

(defn show [bl tag]
  (when (aget (:opts bl) tag)
    (let [blel (:el bl)
          panel (.find blel (str ".border-layout-" tag))
          west (.find blel ".border-layout-west")
          center (.find blel ".border-layout-center")
          panel-size (cond
                      (= :west tag) (.outerWidth panel)
                      (= :east tag) (.outerWidth panel)
                      (= :north tag) (.outerHeight panel)
                      (= :south tag) (.outerHeight panel))
          opts (aget (:opts bl) tag)]
      (when (or (= :west tag) (= :east tag))
        (.animate panel {:width (:size opts)} 300)
        (.animate center {:marginLeft (str (:size opts) "px")
                          :width (- (.width center)
                                    (:size opts))} 300))
      (when (or (= :north tag) (= :south tag))
        (.animate panel {:height (:size opts)} 300)
        (.animate center {:height (- (.height center)
                                     (:size opts))} 300)
        (.animate west {:height (- (.height center)
                                   (:size opts))}
                  300)))))

(defn set-center [bl new-center]
  (let [center-wrap (.find (:el bl) ".border-layout-center")]
    (.empty center-wrap)
    (append center-wrap new-center)
    (.layout bl)))

(defn make-north [opts]
  (let [height (or (:size opts)
                   50)]
    (doto ($html [:div {:class "border-layout-north"}
                  opts])
      (css {:height height}))))

(defn make-west [opts]
  (when opts
    (let [width (or (:size opts)
                    50)]
      (doto ($html [:div {:class "border-layout-west"}
                    opts])
        (css {:width width
              :float "left"})))))

(defn make-east [opts]
  (when opts
    (let [width (or (:size opts)
                    50)]
      (doto ($html [:div {:class "border-layout-east"}
                    opts])
        (css {:width width})))))

(defn make-south [opts]
  (when opts
    (let [height (or (:size opts)
                    50)]
      (doto ($html [:div {:class "border-layout-south"}
                    opts])
        (css {:height height})))))

(defn height [el]
  (cond
   (not el) 0
   :else (.height el)))

(defn width [el]
  (cond
   (not el) 0
   :else (.width el)))

(defn outer-width [el]
  (cond
   (not el) 0
   :else (.outerWidth el)))

(defn layout [& o]
  (let [opts (if (string? (first o))
               (apply hash-map o)
               (first o))]
    (when (not (:center opts))
      (throw ":center is requried for border layout, but not found in opts."))
    (let [center (if (has-el? (:center opts))
                   (:el (:center opts))
                   (:center opts))
          north (make-north (:north opts))
          west (make-west (:west opts))
          east (make-east (:east opts))
          south (make-south (:south opts))
          wrap ($html
                [:div {:class (str "border-layout " (:clss opts))}
                 (when north north)
                 (when west west)
                 (when east east)
                 (:center opts)
                 (when south south)])
          layout (fn []
                   (let [total-height (height wrap)
                         north-height (height north)
                         south-height (height south)
                         center-height (- total-height north-height south-height)
                         total-width (width wrap)
                         west-width (width west)
                         east-width (width east)
                         center-width (- total-width west-width east-width)]
                     (println (.outerWidth wrap))
                     (css center {:height center-height
                                  :width center-width
                                  :marginLeft west-width})
                     (css west {:height center-height})
                     (css east {:float :right
                                :height center-height})
                     (when (has-layout? (:center opts))
                       (.layout (:center opts)))))]
      (css wrap {:height "100%"
                 :width "100%"
                 :padding 0
                 :margin 0})
      (layout)
      {:el wrap
       :layout layout})))


