(ns big-time.ui.clock
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big-time.util.time :as time]))

(declare TickingClock)

(defn- tick [data-atom]
  (let [time-vector (time/current-time-vector)]
    (swap! data-atom assoc :current-time time-vector)
    (if (= (:page @data-atom) TickingClock)
      (.setTimeout js/window (partial tick data-atom) 500))))

(defn- set-doc-title-as-time [data]
  (set! (.-title js/document) (string/join ":" (:current-time data))))

(q/defcomponent Clock
  :name "Clock"
  [time]
  (let [[hours minutes seconds] time]
    (dom/div {:className "clock"}
      (dom/span {:className "clock__hour"} hours)
      (dom/span {:className "clock__minute"} ":" minutes)
      (dom/span {:className "clock__second"} ":" seconds))))

(q/defcomponent TickingClock
  :name "TickingClock"
  :on-mount #(tick %3)
  :on-update #(set-doc-title-as-time %2)
  [data data-atom]
  (Clock (:current-time data)))
