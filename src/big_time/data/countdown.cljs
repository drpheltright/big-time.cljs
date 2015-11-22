(ns big-time.data.countdown)

(defn set-form-time [data-atom field value]
  (swap! data-atom assoc-in [:countdown :form field] value))

(defn get-form-time [data-atom field]
  (get-in @data-atom [:countdown :form field]))
