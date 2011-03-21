(defproject cljs-contrib "0.2"
  :description "Random cljs libraries"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  :dev-dependencies [[swank-clojure "1.2.0"]
                     [cljs "0.2"]
                     [lein-cljs "0.2"]
                     [lein-clojars "0.6.0"]]
  :cljs {:source-path "src"
         :source-output-path "jsout"
         :test-output-path "jsout"
         :test-path "test"
         :test-libs [html-test
                     util-test
                     widgets-test
                     functional.border-layout
                     functional.card-layout
                     functional.panel-widgets]})
