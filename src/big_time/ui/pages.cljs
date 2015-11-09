(ns big-time.ui.pages
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom])
  (:require-macros [big-time.util.markdown :as md]))

(q/defcomponent About
  :name "About"
  :on-render #(set! (.-title js/document) "About")
  [data data-atom]
  (dom/div {:className "page page--about"}
    (md/component "markdown/about.md")))
