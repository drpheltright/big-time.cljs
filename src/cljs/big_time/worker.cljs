(ns big-time.worker)

(defn- process
  "Maps over tasks, an array of functions"
  [tasks]
  (doall (map (fn [task] (task)) tasks)))

(defn create
  "Creates a new worker that executes an array of functions returned from
   find-fn every 500 milliseconds"
  [find-fn]
  (letfn [(process-tick []
            (process (find-fn))
            (js/setTimeout process-tick 500))]
    (process-tick)))
