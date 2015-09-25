(ns big-time.core
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [goog.dom :as gdom]
            [big-time.ui.app :as app]
            [big-time.ui.clock :as clock]))

(enable-console-print!)

(def data (atom {:backgrounds [:red :blue :green]
                 :current-time nil}))

(defn time-str [int]
  (if (= (count (str int)) 1)
    (str "0" int)
    (str int)))

(defn current-time [date]
  [(time-str (.getHours date))
   (time-str (.getMinutes date))
   (time-str (.getSeconds date))])

(defn render []
  (let [now (current-time (js/Date.))]
    (swap! data #(update-in % [:current-time] (fn [_] now)))
    (q/render (app/App @data clock/Clock data) (gdom/getElement "app"))))

(defn init []
  (render)
  (.requestAnimationFrame js/window init))
