(ns widgets
  (:use html
        util)
  (:import [jQuery :as $]))

;; Tab Panel

(defn tab [title content]
  (let [
        link ($html [:a {:href "#"} title])]
    {:title link
     :content content
     :focus #(do (.addClass link "selected")
                 (.css content {:display "block"}))
     :blur #(do (.removeClass link "selected")
                (.css content {:display "none"}))
     :click #(.click link %)}))

(defn tab-panel [& tabs]
  (let [el (v-split-pane {:top ($html [:div {:class "tabs"}
                                       (map #(:title %) tabs)
                                       [:div {:class "clear"}]])
                          :bottom ($html [:div {:class "tab-content"}
                                          (map #(:content %) tabs)])
                          :splitter {:pos 36}
                          :class "tab-panel"})]
    (doseq [t tabs]
      (.click t (fn []
                  (map #(.blur %) tabs)
                  (.focus t)
                  false))
      (.blur t))
    (.focus (first tabs))
    el))

(defn layout-css [name]
  (cond
   (= :fill name) fill-css
   :else {}))

(def wrap-css {:width "100%"
               :height "100%"
               :position :relative})

(def fill-css {:position :absolute
               :top 0
               :left 0
               :right 0
               :bottom 0})

(defn panel [& o]
  (let [opts (if (string? (first o))
               (apply hash-map o)
               (first o)) 
        content ($html [:div {:class "panel-content"}])
        wrap ($html [:div {:class "panel"} content])
        layout (or (:layout opts) :fill)]
    (append content (:items opts))
    (css wrap wrap-css)
    (when (:style opts)
      (css content (:style opts)))
    (css content (layout-css layout))
    {:el wrap
     :size (:size opts)}))

(defn window-frame [& o]
  (let [opts (apply hash-map o)
        container (or (:container opts)
                      ($ "body"))
        el ($html [:div {:class "window-frame"}
                   (:content opts)])
        layout #(when (has-layout? (:content opts))
                  (.layout (:content opts)))
        html ($ "html")
        body ($ "body")]
    (when (:style opts)
      (css el (:style opts)))
    (css html {:width "100%"
               :height "100%"
               :padding 0
               :margin 0})
    (css body {:width "100%"
               :height "100%"
               :padding 0
               :margin 0})
    (css el fill-css)
    (.empty container)
    (append container el)
    (.resize ($ 'window) #(layout))
    (layout)
    {:el container
     :layout layout}))

(defn border-layoutt [& options]
  (let [opts (apply hash-map options)
        north (:north opts)
        north-el ($html [:div {:class "border-layout-north"} (when north (:el north))])
        south (:south opts)
        south-el ($html [:div {:class "border-layout-south"} (when south (:el south))])
        east (:east opts)
        east-el ($html [:div {:class "border-layout-east"} (when east (:el east))])
        west (:west opts)
        west-el ($html [:div {:class "border-layout-west"} (when west (:el west)) ])
        center (:center opts)
        center-el ($html [:div {:class "border-layout-center"} center])
        container-el ($html [:div {:class (str "border-layout " (:clss opts))}
                             (when north
                               north-el)
                             (when east
                               east-el)
                             (when west
                               west-el)
                             (when center
                               center-el)
                             (when south
                               south-el)])

        layout (fn []
                 (let [total-height (.outerHeight container-el)
                       center-height (- total-height
                                        (if north
                                          (.height north-el)
                                          0)
                                        (if south
                                          (.height south-el)
                                          0))
                       total-width (.width container-el)
                       center-width (- total-width
                                       (if west
                                         (.width west-el)
                                         0)
                                       (if east
                                         (.width east-el)
                                         0))]
                   (println total-width center-width)
                   (.height west-el (if west center-height 0))
                   (.height east-el (if east center-height 0))
                   (.css center-el {:width center-width
                                    :height center-height
                                    :position :relative
                                    :margin-left (if west
                                                   (.width west-el)
                                                   0)})))]
    (when west (.css west-el {:float "left"}))
    (.height north-el (if north (or (:size north) 50) 0))
    (.height south-el (if south (or (:size south) 50) 0))
    (.width west-el (if west (or (:size west) 200) 0))
    (.width east-el (if east (or (:size east) 200) 0))
    (.css container-el {:position "relative"
                        :padding 0
                        :margin 0
                        :width "100%"
                        :height "100%"})
    (layout)
    {:el container-el
     :layout layout
     :north-el north-el
     :west-el west-el
     :east-el east-el
     :south-el south-el
     :opts opts}))

(defn select-item [item]
  (let [source-list ($ (first (.parents item ".source-list")))]
    (.removeClass (.find source-list ".selected") "selected")
    (.addClass item "selected")))

(defn render-item [item]
  (let [item-el ($html [:li {:class "source-list-item"}
                        [:span {:class "item-icon"}
                         (if (:icon item)
                           [:img {:src (:icon item)}]
                           "&nbsp;")]
                        [:span {:class "item-name"} (:name item)]])]
    (.click item-el (:handler item))
    (.click item-el #(select-item item-el))
    item-el))

(defn render-category [cat]
  (let [expander ($html [:div {:class "category-expander open"}])
        items ($html [:ul {:class "source-list-items"}
                      (map render-item (:items cat))])
        items-el ($html [:div {:class "items-container"}
                         items])]
    (.click expander
            #(if (> (.indexOf (.attr expander :class) :open) -1)
               (do (.removeClass expander :open)
                   (.addClass expander :closed)
                   (.css items {:display :none}))
               (do (.removeClass expander :closed)
                   (.addClass expander :open)
                   (.css items {:display :block}))))
    {:el [:li {:class "source-list-category"}
          expander
          [:div {:class "category-name"} (:name cat)]
          [:div {:class "clear"}]
          items-el]
     :addItem (fn [item]
                (let [new-items (doto ($html [:ul {:class "source-list-items"}
                                              (map render-item (:items cat))])
                                  (append (render-item item)))]))}))

;;         categories (map make-categories (:categories opts))


(defn source-list [options]
  (let [opts options
        categories-els (map render-category (:categories opts))
        el ($html [:div {:class (str "source-list " (:clss opts))}
                   [:ul {:class "source-list-categories"}
                    (map #(:el %) categories-els)]])]
    {:el el
     :addToCategory (fn []
                      )}))



(defn jquery? [o]
  (instanceof o 'jQuery))

(defn has-el? [o]
  (when o
    (aget o :el)))

(defn percent? [s]
  (and (string? s)
       (> (.indexOf s "%") -1)))

(defn parse-percent [s]
  (-> s
      (.replace "%" "")
      ('parseFloat)
      (/ 100)))

(defn size-h-split-pane [container left-el right-el opts]
  (let [w (.width container)
        h (.height container)
        split-pos (or (:pos (:splitter opts))
                      200)
        split-width (or (:size (:splitter opts))
                        (if (:dynamic (:splitter opts))
                          10
                          0))
        left-width (cond
                    (percent? split-pos) (* w (parse-percent split-pos))
                    :else split-pos)]
    
    (css left-el {:height h
                  :width (- left-width
                            split-width)})
    (css right-el {:height h
                   :width (- w left-width)})))

(defn v-splitter [container left-el right-el opts]
  (let [el (.css ($html [:div {:class "v-splitter"}])
                 {:width (or (:size (:splitter opts))
                             (if (:dynamic (:splitter opts))
                               10
                               0))
                  :height (.height container)
                  :padding 0
                  :margin 0
                  :float "left"})
        dragging false
        last-x 0
        body ($ "body")
        shim (.css ($html [:div])
                   {:zIndex 9999
                    :width (.width body)
                    :height (.height body)
                    :backgroundColor "transparent"
                    :position "fixed"
                    :top 0
                    :left 0})]
    (when (:dynamic (:splitter opts))
      (.css el :cursor "col-resize")
      (.mousedown el (fn [e]
                       (set! dragging true)
                       (set! last-x e.clientX)
                       (.append body shim)))
      (.mousemove ($ "body") (fn [e]
                               (if dragging
                                 (let [delta (- e.clientX last-x)]
                                   (set! last-x e.clientX)
                                   (.width left-el (+ (.width left-el)
                                                      delta))
                                   (.width right-el (- (.width right-el)
                                                       delta))
                                   (when (has-layout? (:left opts))
                                     (.layout (:left opts)))
                                   (when (has-layout? (:right opts))
                                     (.layout (:right opts)))))))
      (.mouseup ($ "body") (fn []
                             (set! dragging false)
                             (.remove shim))))
    el))


(defn h-split-pane [o]
  (let [split-opts (merge {:pos 200
                           :size 10
                           :dynamic false}
                          (:splitter o))
        opts (merge {:splitter split-opts}
                    o)
        left-el (if (has-el? (:left opts))
                  (:el (:left opts))
                  (:left opts))
        right-el (if (has-el? (:right opts))
                   (:el (:right opts))
                   (:right opts))
        container (doto ($html [:div {:class "h-split-pane"}])
                    (.css {:width "100%"
                           :height "100%"
                           :overflow "hidden"}))
        left-pane ($html [:div {:class "left-pane" :style "float: left;  margin: 0px; padding: 0px; position: relative;"}
                          left-el])
        right-pane ($html [:div {:class "right-pane" :style "float: left; margin: 0px; padding: 0px; position: relative"}
                           right-el])
        splitter (v-splitter container left-pane right-pane opts)]
    (append container left-pane)
    (append container splitter)
    (append container right-pane)
    (append container ($html [:div {:style "clear: both"}]))
    {:el container
     :layout (fn []
               (.height splitter (.height container))
               (size-h-split-pane container left-pane right-pane opts)
               (when (has-layout? (:left opts))
                 (.layout (:left opts)))
               (when (has-layout? (:right opts))
                 (.layout (:right opts))))}))


(defn size-v-split-pane [container top-el bottom-el opts]
  (when (and container top-el bottom-el)
    (let [w (.outerWidth container)
          h (.outerHeight container)
          splitter-size (if (:dynamic opts)
                          (:size opts))
          top-height (:pos opts)
          bottom-height (- h top-height)]
      (.css top-el {:height top-height})
      (.css bottom-el {:height bottom-height}))))

(defn mk-shim [body]
  (css ($html [:div])
       {:zIndex 9999
        :width (.width body)
        :height (.height body)
        :backgroundColor "transparent"
        :position "fixed"
        :top 0
        :left 0}))


(defn h-splitter [container top-el bottom-el opts]
  (let [el ($html [:div {:class "h-splitter"}])
        dragging false
        last-y 0
        body ($ "body")
        shim (mk-shim body)]
    (.mousedown el (fn [e]
                     (set! dragging true)
                     (set! last-y e.clientY)
                     (.append body shim)))
    (.mousemove body (fn [e]
                       (if dragging
                         (let [delta (- e.clientY last-y)]
                           (set! last-y e.clientY)
                           (.height top-el (+ (.height top-el)
                                              delta))
                           (.height bottom-el (- (.height bottom-el)
                                                 delta))))))
    (.mouseup body (fn []
                     (set! dragging false)
                     (.remove shim)))
    el))


(defn v-split-pane [o]
  (let [split-opts (merge {:pos 300
                           :size 10
                           :dynamic false}
                          (:splitter o))
        opts (merge o {:splitter split-opts})
        container (doto ($html [:div {:class (str "v-split-pane "
                                                  (:class opts))}])
                    (.css {:width "100%"
                           :height "100%"
                           :overflow "hidden"}))
        top-el (if (has-el? (:top opts))
                 (:el (:top opts))
                 (:top opts))
        bottom-el (if (has-el? (:bottom opts))
                    (:el (:bottom opts))
                    (:bottom opts))
        top-pane ($html [:div {:class "top-pane"
                               :style "position: relative;"}
                         top-el])
        bottom-pane ($html [:div {:class "bottom-pane"
                                  :style "position: relative"}
                            bottom-el])
        splitter (h-splitter container top-pane bottom-pane split-opts)]
    (append container top-pane)
    (if (:dynamic (:splitter opts))
      (append container splitter))
    (append container bottom-pane)
    {:el container
     :layout (fn []
               (.width splitter (.width container))
               (size-v-split-pane container top-pane bottom-pane split-opts)
               (when (has-layout? (:top opts))
                 (.layout (:top opts)))
               (when (has-layout? (:bottom opts))
                 (.layout (:bottom opts))))}))


(defn on-escape [f]
  (fn [e]
    (let [kc e.keyCode]
      (when (= kc 27)
        (f e)))))

(defn on-enter [f]
  (fn [e]
    (let [kc e.keyCode]
      (when (= kc 13)
        (f e)))))


(defn make-editable [el initial enter]
  (.addClass el "editable")
  (.click el (fn []
               (let [input ($html [:input {:type "text"}])
                     et ($html [:div {:class "editing-thing"}
                                input])]
                 (.keyup input (on-escape (fn []
                                            (.replaceWith et #(make-editable
                                                               el
                                                               (.val input)
                                                               enter)))))
                 (.keyup input (on-enter (fn []
                                           (when (enter (.val input))
                                             (.replaceWith et #(make-editable
                                                                el
                                                                (.val input)
                                                                enter))))))
                 (.val input initial)
                 (.replaceWith el et)
                 (.focus input)
                 (.select input))))
  el)


