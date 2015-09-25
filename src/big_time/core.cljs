(ns big-time.core
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [goog.dom :as gdom]
            [clojure.string :as string]))

(enable-console-print!)

(def data (atom {:backgrounds [:red :blue :green]}))

(defn time-str [int]
  (if (= (count (str int)) 1)
    (str "0" int)
    (str int)))

(defn current-time [date]
  [(time-str (.getHours date))
   (time-str (.getMinutes date))
   (time-str (.getSeconds date))])

(defn change-background [_]
  (swap! data (fn [data]
                (update-in data [:backgrounds]
                  (fn [backgrounds]
                    (let [new-background (peek backgrounds)]
                      (apply conj [new-background] (pop backgrounds))))))))

(q/defcomponent Clock
  [date]
  (dom/div {:className "clock"
            :style {:background (first (:backgrounds @data))}
            :onClick change-background}
    (dom/div {:className "clock__time"}
      (dom/span {:className "clock__hour"} (get date 0))
      (dom/span {:className "clock__minute"} ":" (get date 1))
      (dom/span {:className "clock__second"} ":" (get date 2)))))

(defn render []
  (let [date (current-time (js/Date.))]
    (set! (.-title js/document) (string/join ":" date))
    (q/render (Clock date) (gdom/getElement "app"))))

(defn init []
  (render)
  (.requestAnimationFrame js/window init))
