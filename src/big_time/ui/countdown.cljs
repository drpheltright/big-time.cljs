(ns big-time.ui.countdown
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big_time.ui.clock :as clock]))

(defn- start-countdown [data-atom e]
  (.preventDefault e)
  (let [time-vector (vals (get-in @data-atom [:countdown :form]))]
    (swap! data-atom assoc-in [:countdown :time] time-vector)))

(defn- is-counting-down? [time]
  (not= time ["00" "00" "00"]))

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

(q/defcomponent Countdown
  :name "Countdown"
  [data data-atom]
  (let [time (get-in data [:countdown :time])]
    (if (is-counting-down? time)
      (clock/Clock time)
      (CountdownForm data data-atom))))
