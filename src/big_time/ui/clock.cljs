(ns big-time.ui.clock
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]
            [big-time.ui.app :as app]))

(defn- set-doc-title-as-time [data]
  (set! (.-title js/document) (string/join ":" (:current-time data))))

(q/defcomponent Clock
  :name "Clock"
  :on-mount #(render-tick %3)
  :on-update #(set-doc-title-as-time %2)
  [data]
  (dom/div {:className "clock"}
    (dom/span {:className "clock__hour"} (get (:current-time data) 0))
    (dom/span {:className "clock__minute"} ":" (get (:current-time data) 1))
    (dom/span {:className "clock__second"} ":" (get (:current-time data) 2))))

(defn- time-str [int]
  (if (= (count (str int)) 1)
    (str "0" int)
    (str int)))

(defn- current-time [date]
  [(time-str (.getHours date))
   (time-str (.getMinutes date))
   (time-str (.getSeconds date))])

(defn- render-tick [data-atom]
  (let [now (current-time (js/Date.))]
    (swap! data-atom #(assoc % :current-time now))
    (if (= (:path @data-atom) "/")
      (.setTimeout js/window (partial render-tick data-atom) 500))))
