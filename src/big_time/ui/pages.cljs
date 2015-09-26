(ns big-time.ui.pages
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [big-time.ui.app :as app]))

(q/defcomponent About
  :name "About"
  :on-render #(set! (.-title js/document) "About")
  [data]
  (dom/div {:className "page page--about"}
    "About Big Time"))

(defn render-about [data]
  (app/render About data))
