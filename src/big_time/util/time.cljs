(ns big-time.util.time)

(defn- time-str [int]
  (if (= (count (str int)) 1)
    (str "0" int)
    (str int)))

(defn- date->time-vector [date]
  [(time-str (.getHours date))
   (time-str (.getMinutes date))
   (time-str (.getSeconds date))])

(defn current-time-vector []
  (date->time-vector (js/Date.)))
