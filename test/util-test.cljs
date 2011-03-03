(ns util-test
  (:use util test-helpers html)
  (:import [jQuery :as $]))

(defn do-append [thing]
  (append ($html [:div])
          thing))

(deftest :test-has-el
  #(is (has-el? {:el "my-el"}))
  #(is (not (has-el? {}))))

(deftest :test-append
  #(is (> (count (.find (do-append ($html [:div {:class "child"}]))
                        ".child"))
          0))
  #(is (> (count (.find (do-append {:el ($html [:div {:class "el-child"}])})
                        ".el-child"))
          0))
  #(is (> (count (.find (do-append [($html [:div {:class "arr-child-1"}])
                                    ($html [:div {:class "arr-child-2"}])])
                        ".arr-child-2"))
          0)))

