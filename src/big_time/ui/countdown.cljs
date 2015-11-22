(ns big-time.ui.countdown
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big_time.ui.clock :as clock]
            [big_time.ui.form :as form]
            [big_time.util.time :as time]
            [big-time.data.countdown :as data]))

(declare Countdown)

(defn- stop-tick [data-atom]
  (swap! data-atom update :tasks dissoc :countdown-tick))

(defn- handle-field-change [e data-atom]
  (let [field (.. e -target -name)
        value (.. e -target -value)]
    (data/set-form-time data-atom (keyword field) value)))

(defn- counting-down? [data]
  (not (nil? (get-in data [:countdown :start-time]))))

(defn- stop-countdown [data-atom]
  (swap! data-atom assoc-in [:countdown :start-time] nil))

(defn- set-current-time [data-atom seconds-left]
  (swap! data-atom assoc-in [:countdown :current-time] (time/seconds->vector seconds-left)))

(defn- complete-countdown [data-atom]
  (js/alert "Countdown complete!")
  (stop-countdown data-atom))

(defn- tick [data data-atom]
  (let [{:keys [start-time duration]} (:countdown data)]
    (if start-time
      (if-let [seconds-left (time/seconds-left start-time duration)]
        (set-current-time data-atom seconds-left)
        (complete-countdown data-atom))
      (stop-tick data-atom))))

(defn- start-tick [data-atom]
  (tick @data-atom data-atom)
  (swap! data-atom update :tasks assoc :countdown-tick #(tick @data-atom data-atom)))

(defn- start-countdown [data-atom e]
  (.preventDefault e)
  (let [time-vector (vals (get-in @data-atom [:countdown :form]))]
    (swap! data-atom update-in [:countdown] assoc :start-time (time/now)
                                                  :duration (time/vector->seconds time-vector))
    (start-tick data-atom)))

(q/defcomponent CountdownForm
  :name "CountdownForm"
  [data data-atom]
  (dom/form {:onChange #(handle-field-change % data-atom)
             :onSubmit (partial start-countdown data-atom)}
    (form/input :countdown :hours (data/get-form-time data-atom :hours))
    (form/input :countdown :minutes (data/get-form-time data-atom :minutes))
    (form/input :countdown :seconds (data/get-form-time data-atom :seconds))
    (form/submit "Start countdown")))

(q/defcomponent Countdown
  :name "Countdown"
  :on-mount #(start-tick %3)
  [data data-atom]
  (if (counting-down? data)
    (dom/div {}
      (clock/Clock (get-in data [:countdown :current-time]))
      (dom/button {:onClick (partial stop-countdown data-atom)} "Stop countdown"))
    (CountdownForm data data-atom)))
