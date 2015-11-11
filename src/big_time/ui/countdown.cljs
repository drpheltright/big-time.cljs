(ns big-time.ui.countdown
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big_time.ui.clock :as clock]
            [big_time.util.time :as time]))

(defn- start-countdown [data-atom e]
  (.preventDefault e)
  (let [time-vector (vals (get-in @data-atom [:countdown :form]))]
    (swap! data-atom update-in [:countdown] assoc :start-time (time/now)
                                                  :duration (time/vector->seconds time-vector))))

(defn- set-form-time [field data-atom e]
  (let [value (.. e -target -value)]
    (swap! data-atom assoc-in [:countdown :form field] value)))

(defn- get-form-time [field data]
  (get-in data [:countdown :form field]))

(defn- counting-down? [data]
  (not (nil? (get-in data [:countdown :start-time]))))

(q/defcomponent CountdownForm
  :name "CountdownForm"
  [data data-atom]
  (dom/form {:onSubmit (partial start-countdown data-atom)}
    (dom/div {:className "countdown__field"}
      (dom/label {:forHtml "countdown_hours"} "Hours")
      (dom/input {:id "countdown_hours"
                  :onChange (partial set-form-time :hours data-atom)
                  :value (get-form-time :hours data)}))
    (dom/div {:className "countdown__field"}
      (dom/label {:forHtml "countdown_minute"} "Minutes")
      (dom/input {:id "countdown_minute"
                  :onChange (partial set-form-time :minutes data-atom)
                  :value (get-form-time :minutes data)}))
    (dom/div {:className "countdown__field"}
      (dom/label {:forHtml "countdown_second"} "Seconds")
      (dom/input {:id "countdown_second"
                  :onChange (partial set-form-time :seconds data-atom)
                  :value (get-form-time :seconds data)}))
    (dom/div {:className "countdown__field"}
      (dom/input {:type "submit" :value "Start clock"}))))

(declare Countdown)

(defn- tick [data data-atom]
  (let [{:keys [start-time duration]} (:countdown data)
        seconds-left (time/seconds-left start-time duration)]
    (when (> seconds-left 0)
      (swap! data-atom assoc-in [:countdown :current-time] (time/seconds->vector seconds-left))
      (if (= (:page data) Countdown)
        (.setTimeout js/window (partial tick data data-atom) 1000)))))

(q/defcomponent Countdown
  :name "Countdown"
  [data data-atom]
  (if (counting-down? data)
    (clock/Clock (get-in data [:countdown :current-time]) (partial tick data data-atom))
    (CountdownForm data data-atom)))
