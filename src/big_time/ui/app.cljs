(ns big-time.ui.app
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]))

(defn change-background [data-atom _]
  (swap! data-atom #(update-in % [:backgrounds]
                     (fn [backgrounds]
                       (let [new-background (peek backgrounds)]
                         (apply conj [new-background] (pop backgrounds)))))))

(q/defcomponent App
  :name "App"
  [data page-component data-atom]
  (dom/div {:className "app"
            :style {:background (first (:backgrounds data))}
            :onClick (partial change-background data-atom)}
    (dom/nav {:className "app__nav"}
      (dom/a {:href "#/"} "clock")
      (dom/a {:href "#/about"} "about"))
    (page-component data data-atom)))
