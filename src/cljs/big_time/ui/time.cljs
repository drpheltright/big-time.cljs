(ns big-time.ui.time
  (:require [quiescent.core :as q]
            [big-time.data.time :as data]
            [big-time.ui.clock :as clock]))

(q/defcomponent Time
  :name "Time"
  :on-mount #(data/start-tick %3)
  :on-unmount #(data/stop-tick %3)
  [data store]
  (clock/Clock (:current-time data)))
