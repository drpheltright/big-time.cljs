(ns big-time.data.countdown
  (:require [big_time.util.time :as time]))

(defn set-form-time [data-atom field value]
  (swap! data-atom assoc-in [:countdown :form field] value))

(defn get-form-time [data-atom field]
  (get-in @data-atom [:countdown :form field]))

(defn counting-down? [data-atom]
  (not (nil? (get-in @data-atom [:countdown :start-time]))))

(defn stop-countdown [data-atom]
  (swap! data-atom assoc-in [:countdown :start-time] nil))

(defn- set-current-time [data-atom seconds-left]
  (swap! data-atom assoc-in [:countdown :current-time] (time/seconds->vector seconds-left)))

(defn get-current-time [data-atom]
  (get-in @data-atom [:countdown :current-time]))

(defn- register-tick [data-atom fn]
  (swap! data-atom update :tasks assoc :countdown-tick fn))

(defn- deregister-tick [data-atom]
  (swap! data-atom update :tasks dissoc :countdown-tick))

(defn- tick [data-atom handle-complete]
  (let [{:keys [start-time duration]} (:countdown @data-atom)]
    (if start-time
      (if-let [seconds-left (time/seconds-left start-time duration)]
        (set-current-time data-atom seconds-left)
        (do
          (stop-countdown data-atom)
          (handle-complete)))
      (deregister-tick data-atom))))

(defn start-tick [data-atom handle-complete]
  (letfn [(composed-tick [] (tick data-atom handle-complete))]
    (composed-tick)
    (register-tick data-atom composed-tick)))

(defn start-countdown [data-atom handle-complete]
  (let [time-vector (vals (get-in @data-atom [:countdown :form]))]
    (swap! data-atom update-in [:countdown] assoc :start-time (time/now)
                                                  :duration (time/vector->seconds time-vector))
    (start-tick data-atom handle-complete)))
