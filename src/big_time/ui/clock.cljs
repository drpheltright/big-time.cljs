(ns big-time.ui.clock
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big-time.util.time :as time]))

(declare Clock)

(defn- tick [data-atom]
  (let [time-vector (time/current-time-vector)]
    (swap! data-atom #(assoc % :current-time time-vector))
    (if (= (:page @data-atom) Clock)
      (.setTimeout js/window (partial tick data-atom) 500))))

(defn- set-doc-title-as-time [data]
  (set! (.-title js/document) (string/join ":" (:current-time data))))

(q/defcomponent Clock
  :name "Clock"
  :on-mount #(tick %3)
  :on-update #(set-doc-title-as-time %2)
  [data]
  (dom/div {:className "clock"}
    (dom/span {:className "clock__hour"} (get (:current-time data) 0))
    (dom/span {:className "clock__minute"} ":" (get (:current-time data) 1))
    (dom/span {:className "clock__second"} ":" (get (:current-time data) 2))))
