(ns big-time.data.time
  (:require [big-time.util.time :as time]))

(defn- tick [data-atom]
  (let [time-vector (time/current-time-vector)]
    (swap! data-atom assoc :current-time time-vector)))

(defn start-tick [data-atom]
  (tick data-atom)
  (swap! data-atom update :tasks assoc :time-tick (partial tick data-atom)))

(defn stop-tick [data-atom]
  (swap! data-atom update :tasks dissoc :time-tick))
