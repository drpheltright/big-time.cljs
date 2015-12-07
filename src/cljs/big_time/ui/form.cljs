(ns big-time.ui.form
  (:require [quiescent.dom :as dom]
            [clojure.string :as string]))

(defn input
  [form field options]
  (dom/div {:className (get options :className "field")}
    (let [form (name form)
          field (name field)
          id (str form "__" field)
          label (str (string/capitalize field))]
      [(dom/label {:forHtml id} label)
       (dom/input {:id id :name field :value (:value options)})])))

(defn input-of-3 [form field options]
  (input form field (merge options {:className "field--of-3"})))

(defn button [& args]
  (dom/div {:className "field--button"}
    (apply dom/button args)))
