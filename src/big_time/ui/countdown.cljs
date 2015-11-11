(ns big-time.ui.countdown
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big_time.ui.clock :as clock]
            [big_time.util.time :as time]))

(defn- start-countdown [data-atom e]
  (.preventDefault e)
  (let [time-vector (vals (get-in @data-atom [:countdown :form]))]
    (swap! data-atom assoc-in [:countdown :start-time] (time/now))
    (swap! data-atom assoc-in [:countdown :duration] (time/vector->seconds time-vector))))

(defn- set-form-time [field data-atom e]
  (let [value (.. e -target -value)]
    (swap! data-atom assoc-in [:countdown :form field] value)))

(defn- get-form-time [field data]
  (get-in data [:countdown :form field]))

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

(defn- tick [data-atom start-time current-time duration]
  (let [end-time (js/Date. start-time)
        end-time-seconds (.setSeconds end-time (+ (.getSeconds start-time) duration))
        seconds-left (/ (- (.getTime end-time) (.getTime (time/now))) 1000)]
    (when (> seconds-left 0)
      (swap! data-atom assoc-in [:countdown :current-time] (time/seconds->vector seconds-left))
      (if (= (:page @data-atom) Countdown)
        (.setTimeout js/window (partial tick data-atom start-time current-time duration) 1000)))))

(q/defcomponent Countdown
  :name "Countdown"
  [data data-atom]
  (let [start-time (get-in data [:countdown :start-time])
        current-time (get-in data [:countdown :current-time])
        duration (get-in data [:countdown :duration])]
    (if-not (nil? start-time)
      (clock/Clock current-time (partial tick data-atom start-time current-time duration))
      (CountdownForm data data-atom))))
