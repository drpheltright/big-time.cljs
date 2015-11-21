(ns big-time.ui.countdown
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big_time.ui.clock :as clock]
            [big_time.util.time :as time]))

(declare Countdown)

(defn- stop-tick [data-atom]
  (swap! data-atom update :tasks dissoc :countdown-tick))

(defn- set-form-time [field data-atom e]
  (let [value (.. e -target -value)]
    (swap! data-atom assoc-in [:countdown :form field] value)))

(defn- get-form-time [field data]
  (get-in data [:countdown :form field]))

(defn- counting-down? [data]
  (not (nil? (get-in data [:countdown :start-time]))))

(defn- stop-countdown [data-atom]
  (swap! data-atom assoc-in [:countdown :start-time] nil))

(defn- tick [data data-atom]
  (let [{:keys [start-time duration]} (:countdown data)]
    (if start-time
      (let [seconds-left (time/seconds-left start-time duration)]
        (if (> seconds-left 0)
          (swap! data-atom assoc-in [:countdown :current-time] (time/seconds->vector seconds-left))
          (do
            (js/alert "Countdown complete!")
            (stop-countdown data-atom))))
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

(q/defcomponent Countdown
  :name "Countdown"
  :on-mount #(start-tick %3)
  [data data-atom]
  (if (counting-down? data)
    (dom/div {}
      (clock/Clock (get-in data [:countdown :current-time]))
      (dom/button {:onClick (partial stop-countdown data-atom)} "stop countdown"))
    (CountdownForm data data-atom)))
