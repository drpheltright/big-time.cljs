(ns big-time.core
  (:require [quiescent.core :refer [render] :refer-macros [defcomponent]]
            [quiescent.dom :as dom]
            [goog.dom :as gdom]
            [clojure.string :as string]))

(enable-console-print!)

(defn time-str [int]
  (if (= (count (str int)) 1)
    (str "0" int)
    (str int)))

(defn current-time [date]
  [(time-str (.getHours date))
   (time-str (.getMinutes date))
   (time-str (.getSeconds date))])

(defcomponent Clock
  [date]
  (dom/div {:className "clock"}
    (dom/span {:className "clock__hour"} (get date 0))
    (dom/span {:className "clock__minute"} ":" (get date 1))
    (dom/span {:className "clock__second"} ":" (get date 2))))

(defn set-doc-title [title]
  (set! (.-title js/document) title))

(defn render-clock [date]
  (render (Clock date) (gdom/getElement "app")))

(defn tick []
  (let [date (current-time (js/Date.))]
    (set-doc-title (string/join ":" date))
    (render-clock date)
    (js/setTimeout tick 500)))

(defn init []
  (tick))
