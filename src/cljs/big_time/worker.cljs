(ns big-time.worker)

(defn- process [tasks]
  (doall (map (fn [task] (task)) tasks)))

(defn create [find-fn]
  (letfn [(process-tick []
            (process (find-fn))
            (js/setTimeout process-tick 500))]
    (process-tick)))
