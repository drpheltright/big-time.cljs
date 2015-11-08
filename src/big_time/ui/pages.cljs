(ns big-time.ui.pages
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [big-time.ui.app :as app])
  (:require-macros [big-time.markdown :as md]))

(q/defcomponent About
  :name "About"
  :on-render #(set! (.-title js/document) "About")
  [data]
  (dom/div {:className "page page--about"}
    (md/component "markdown/about.md")))
