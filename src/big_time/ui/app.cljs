(ns big-time.ui.app
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [big-time.routes :as routes]))

(q/defcomponent App
  :name "App"
  [data page-component data-atom]
  (dom/div {:className "app"}
    (dom/nav {:className "app__nav"}
      (dom/a {:href (routes/component->path :clock)} "clock")
      (dom/a {:href (routes/component->path :countdown)} "countdown")
      (dom/a {:href (routes/component->path :about)} "about"))
    (page-component data data-atom)))
