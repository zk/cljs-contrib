(ns util
  (:import [jQuery :as $]
           RegExp))

(defn has-el? [o]
  (if (and o (aget o :el))
    true
    false))

(defn append [p c]
  (cond
   (array? c) (map (fn [c] (append p c)) c)
   (has-el? c) (append p (:el c))
   :else (do
           (.append p c)
           (when (instanceof c 'jQuery)
             (.trigger c "postinsert"))))
  p)

(defn replace-in [p c]
  (.empty p)
  (append p c))

(defn take [n o]
  (cond
   (string? o) (.substring o 0 n)))

(defn apply-str [ss]
  (reduce (fn [col s] (+ col s)) ss))

(defn ellipsis [s n]
  (if (> 's.length n)
    (str (.substring s 0 n) "...")
    s))

(defn make-url-friendly [s]
  (-> s
      (.replace (RegExp. "-" "g") "_")
      (.replace (RegExp. " " "g") "_")
      (.toLowerCase)))

(defn ready [f]
  (.ready ($ 'document) f))

(defn has-layout? [o]
  (when o
    (aget o :layout)))

(defn ajax [opts]
  (.ajax $ opts))
