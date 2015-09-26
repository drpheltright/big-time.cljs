(ns big-time.ui.pages
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [goog.dom :as gdom]
            [big-time.ui.app :as app]))

(q/defcomponent About
  :name "About"
  :on-render #(set! (.-title js/document) "About")
  [data]
  (dom/div {:className "page page--about"}
    "About Big Time"))

(defn render-about [data]
  (swap! data #(assoc % :ticking false))
  (q/render (app/App @data About data) (gdom/getElement "app")))
