(ns big-time.ui.app
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [big-time.routes :as routes]))

(defn- deal-next-background [backgrounds]
  (let [new-background (peek backgrounds)]
    (apply conj [new-background] (pop backgrounds))))

(defn- change-background [data-atom _]
  (swap! data-atom update-in [:backgrounds] deal-next-background))

(q/defcomponent App
  :name "App"
  [data page-component data-atom]
  (dom/div {:className "app"
            :style {:background (first (:backgrounds data))}
            :onClick (partial change-background data-atom)}
    (dom/nav {:className "app__nav"}
      (dom/a {:href (routes/component->path :clock)} "clock")
      (dom/a {:href (routes/component->path :countdown)} "countdown")
      (dom/a {:href (routes/component->path :about)} "about"))
    (page-component data data-atom)))
