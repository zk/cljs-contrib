(ns html-test
  (:use html test))

(deftest :test-parse-attrs
  #(is (_= {} (parse-attrs [:div ($ "<div />")])))
  #(is (_= {:foo "bar"} (parse-attrs [:div {:foo "bar"} "hi"])))
  #(is (_= {} (parse-attrs [:div "foo"]))))

(deftest :test-parse-body
  #(is (_= ["foo"] (parse-body [:div "foo"])))
  #(is (_= ["foo" "bar"] (parse-body [:div {:class "stuff"} "foo" "bar"]))))

(deftest :test-$html
  #(do 
     (append-body
      ($html [:div {:class "foo"} "FOOO!"]))
     (is (in-dom ".foo"))))

