(ns big-time.data.time
  (:require [big-time.util.time :as time]))

(defn- tick [store]
  (let [time-vector (time/current-time-vector)]
    (swap! store assoc :current-time time-vector)))

(defn start-tick [store]
  (tick store)
  (swap! store update :tasks assoc :time-tick (partial tick store)))

(defn stop-tick [store]
  (swap! store update :tasks dissoc :time-tick))
