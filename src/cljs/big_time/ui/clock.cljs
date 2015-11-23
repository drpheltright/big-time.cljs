(ns big-time.ui.clock
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]))

(defn- set-doc-title-as-time [time]
  (set! (.-title js/document) (string/join ":" time)))

(q/defcomponent Clock
  :name "Clock"
  :on-update #(set-doc-title-as-time %2)
  [time]
  (let [[hours minutes seconds] time]
    (dom/div {:className "clock"}
      (dom/span {:className "clock__hour"} hours)
      (dom/span {:className "clock__minute"} ":" minutes)
      (dom/span {:className "clock__second"} ":" seconds))))
