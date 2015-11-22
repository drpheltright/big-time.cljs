(ns big-time.ui.countdown
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [big_time.ui.clock :as clock]
            [big_time.ui.form :as form]
            [big-time.data.countdown :as data]))

(defn- handle-field-change [e store]
  (let [field (.. e -target -name)
        value (.. e -target -value)]
    (data/set-form-time store (keyword field) value)))

(defn- handle-countdown-complete []
  (js/alert "Countdown complete!"))

(defn- handle-form-submit [e store]
  (.preventDefault e)
  (data/start-countdown store handle-countdown-complete))

(defn- handle-mount [store]
  (data/check-for-countdown store handle-countdown-complete))

(defn- handle-stop-countdown [store]
  (data/stop-countdown store))

(q/defcomponent CountdownForm
  :name "CountdownForm"
  [data store]
  (dom/form {:onChange #(handle-field-change % store)
             :onSubmit #(handle-form-submit % store)}
    (form/input :countdown :hours (data/get-form-time store :hours))
    (form/input :countdown :minutes (data/get-form-time store :minutes))
    (form/input :countdown :seconds (data/get-form-time store :seconds))
    (form/submit "Start countdown")))

(q/defcomponent Countdown
  :name "Countdown"
  :on-mount #(handle-mount %3)
  [data store]
  (if (data/counting-down? store)
    (dom/div {}
      (clock/Clock (data/get-current-time store))
      (dom/button {:onClick #(handle-stop-countdown store)} "Stop countdown"))
    (CountdownForm data store)))
