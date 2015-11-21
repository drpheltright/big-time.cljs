(ns big-time.ui.time
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [big-time.util.time :as time]
            [big-time.ui.clock :as clock]))

(declare Time)

(defn- tick [data-atom]
  (let [time-vector (time/current-time-vector)]
    (swap! data-atom assoc :current-time time-vector)))

(defn- start-tick [data-atom]
  (tick data-atom)
  (swap! data-atom update :tasks assoc :time-tick #(tick data-atom)))

(defn- stop-tick [data-atom]
  (swap! data-atom update :tasks dissoc :time-tick))

(q/defcomponent Time
  :name "Time"
  :on-mount #(start-tick %3)
  :on-unmount #(stop-tick %3)
  [data data-atom]
  (clock/Clock (:current-time data)))
