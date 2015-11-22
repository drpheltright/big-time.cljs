(ns big-time.ui.countdown
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big_time.ui.clock :as clock]
            [big_time.ui.form :as form]
            [big_time.util.time :as time]
            [big-time.data.countdown :as data]))

(defn- handle-field-change [e data-atom]
  (let [field (.. e -target -name)
        value (.. e -target -value)]
    (data/set-form-time data-atom (keyword field) value)))

(defn- complete-countdown [data-atom]
  (js/alert "Countdown complete!")
  (data/stop-countdown data-atom))

(defn- tick [data-atom]
  (let [{:keys [start-time duration]} (:countdown @data-atom)]
    (if start-time
      (if-let [seconds-left (time/seconds-left start-time duration)]
        (data/set-current-time data-atom seconds-left)
        (complete-countdown data-atom))
      (data/stop-tick data-atom))))

(defn- start-tick [data-atom]
  (tick data-atom)
  (data/start-tick data-atom #(tick data-atom)))

(defn- handle-form-submit [e data-atom]
  (.preventDefault e)
  (data/start-countdown data-atom)
  (start-tick data-atom))

(defn- handle-mount [data-atom]
  (start-tick data-atom))

(defn- handle-stop-countdown [data-atom]
  (data/stop-countdown data-atom))

(q/defcomponent CountdownForm
  :name "CountdownForm"
  [data data-atom]
  (dom/form {:onChange #(handle-field-change % data-atom)
             :onSubmit #(handle-form-submit % data-atom)}
    (form/input :countdown :hours (data/get-form-time data-atom :hours))
    (form/input :countdown :minutes (data/get-form-time data-atom :minutes))
    (form/input :countdown :seconds (data/get-form-time data-atom :seconds))
    (form/submit "Start countdown")))

(q/defcomponent Countdown
  :name "Countdown"
  :on-mount #(handle-mount %3)
  [data data-atom]
  (if (data/counting-down? data-atom)
    (dom/div {}
      (clock/Clock (data/get-current-time data-atom))
      (dom/button {:onClick #(handle-stop-countdown data-atom)} "Stop countdown"))
    (CountdownForm data data-atom)))
