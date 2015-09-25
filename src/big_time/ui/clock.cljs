(ns big-time.ui.clock
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [clojure.string :as string]))

(defn set-doc-title [title]
  (set! (.-title js/document) title))

(defn set-doc-title-as-time [data]
  (set-doc-title (string/join ":" (:current-time data))))

(q/defcomponent Clock
  :on-render (fn [_ data] (set-doc-title-as-time data))

  [data]
  (dom/div {:className "clock"}
    (dom/span {:className "clock__hour"} (get (:current-time data) 0))
    (dom/span {:className "clock__minute"} ":" (get (:current-time data) 1))
    (dom/span {:className "clock__second"} ":" (get (:current-time data) 2))))
