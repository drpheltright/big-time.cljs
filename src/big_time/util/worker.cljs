(ns big-time.util.worker)

(defn- process [tasks]
  (println "processing")
  (doall (map (fn [task] (task)) tasks)))

(defn create [find-fn]
  (letfn [(process-tick []
            (process (find-fn))
            (js/setTimeout process-tick 500))]
    (process-tick)))
