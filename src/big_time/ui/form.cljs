(ns big-time.ui.form
  (:require [quiescent.dom :as dom]
            [clojure.string :as string]))

(defn input [form field value]
  (dom/div {:className "field"}
    (let [form (name form)
          field (name field)
          id (str form "__" field)
          label (str (string/capitalize field))]
      [(dom/label {:forHtml id} label)
       (dom/input {:id id :name field :value value})])))
