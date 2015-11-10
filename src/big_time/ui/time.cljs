(ns big-time.ui.time
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [big-time.util.time :as time]
            [big-time.ui.clock :as clock]))

(declare Time)

(defn- tick [data-atom]
  (let [time-vector (time/current-time-vector)]
    (swap! data-atom assoc :current-time time-vector)
    (if (= (:page @data-atom) Time)
      (.setTimeout js/window (partial tick data-atom) 500))))

(q/defcomponent Time
  :name "Time"
  [data data-atom]
  (clock/Clock (:current-time data) (partial tick data-atom)))
