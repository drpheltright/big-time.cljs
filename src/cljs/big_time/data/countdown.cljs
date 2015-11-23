(ns big-time.data.countdown
  (:require [big_time.util.time :as time]))

(defn set-form-time [store field value]
  (swap! store assoc-in [:countdown :form field] value))

(defn get-form-time [store field]
  (get-in @store [:countdown :form field]))

(defn counting-down? [store]
  (not (nil? (get-in @store [:countdown :start-time]))))

(defn stop-countdown [store]
  (swap! store assoc-in [:countdown :start-time] nil))

(defn- set-current-time [store seconds-left]
  (swap! store assoc-in [:countdown :current-time] (time/seconds->vector seconds-left)))

(defn get-current-time [store]
  (get-in @store [:countdown :current-time]))

(defn- register-tick [store fn]
  (swap! store update :tasks assoc :countdown-tick fn))

(defn- deregister-tick [store]
  (swap! store update :tasks dissoc :countdown-tick))

(defn- tick [store handle-complete]
  (let [{:keys [start-time duration]} (:countdown @store)]
    (if start-time
      (if-let [seconds-left (time/seconds-left start-time duration)]
        (set-current-time store seconds-left)
        (do
          (stop-countdown store)
          (handle-complete)))
      (deregister-tick store))))

(defn- start-tick [store handle-complete]
  (letfn [(composed-tick [] (tick store handle-complete))]
    (composed-tick)
    (register-tick store composed-tick)))

(defn start-countdown [store handle-complete]
  (let [time-vector (vals (get-in @store [:countdown :form]))]
    (swap! store update-in [:countdown] assoc :start-time (time/now)
                                                  :duration (time/vector->seconds time-vector))
    (start-tick store handle-complete)))

(defn check-for-countdown [store handle-complete]
  (start-tick store handle-complete))
