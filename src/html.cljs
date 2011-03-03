(ns html
  (:use util)
  (:import [jQuery :as $]))

(defn parse-attrs [args]
  (let [attr-arg (nth args 1)]
    (cond
     (instanceof attr-arg 'jQuery) {}
     (object? attr-arg) attr-arg
     :else {})))

(defn parse-body [args]
  (let [body (cond
              (instanceof (nth args 1) 'jQuery) (drop 1 args)
              (object? (nth args 1)) (drop 2 args)
              :else (drop 1 args))]
    (->> body
         (filter _.identity)
         (filter #(not (= 'undefined %))))))


(defn html [args]
  (cond
   (string? args) args
   (element? args) args
   (element? (first args)) args
   (instanceof args 'jQuery) args
   (array? (first args)) (map html args)
   (has-el? args) (:el args)
   true (let [as (filter _.identity args)
              tag (first as)
              attrs (parse-attrs as)
              body (parse-body as)
              el ($ (str "<" tag "/>"))]
          (if attrs
            (.attr el attrs))
          (append el (map html body)))))

(defn $html [args]
  ($ (html args)))

