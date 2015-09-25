(ns big-time.ui.app
  (:require [quiescent.core :as q]
            [quiescent.dom :as dom]))

(defn change-background [data _]
  (swap! data #(update-in % [:backgrounds]
                 (fn [backgrounds]
                   (let [new-background (peek backgrounds)]
                     (apply conj [new-background] (pop backgrounds)))))))

(q/defcomponent App
  [data InnerComponent data-atom]
  (dom/div {:className "app"
            :style {:background (first (:backgrounds data))}
            :onClick (partial change-background data-atom)}
    (InnerComponent data)))
