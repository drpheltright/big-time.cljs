(ns big-time.ui.app
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]
            [goog.dom :as gdom]))

(defn change-background [data _]
  (swap! data #(update-in % [:backgrounds]
                 (fn [backgrounds]
                   (let [new-background (peek backgrounds)]
                     (apply conj [new-background] (pop backgrounds)))))))

(q/defcomponent App
  :name "App"
  [data InnerComponent data-atom]
  (dom/div {:className "app"
            :style {:background (first (:backgrounds data))}
            :onClick (partial change-background data-atom)}
    (dom/nav {:className "app__nav"}
      (dom/a {:href "#/"} "clock")
      (dom/a {:href "#/about"} "about"))
    (InnerComponent data)))

(defn render [InnerComponent data]
  (q/render (App @data InnerComponent data) (gdom/getElement "app")))
