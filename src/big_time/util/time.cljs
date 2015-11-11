(ns big-time.util.time)

(defn time-str [int]
  (if (= (count (str int)) 1)
    (str "0" int)
    (str int)))

(defn- str->int [str]
  (js/parseInt str 10))

(defn- date->vector [date]
  [(time-str (.getHours date))
   (time-str (.getMinutes date))
   (time-str (.getSeconds date))])

(defn vector->seconds [time-vector]
  (let [[hours minutes seconds] (map str->int time-vector)]
    (+ (* hours 60 60) (* minutes 60) seconds)))

(defn seconds->vector [seconds]
  (date->vector (js/Date. (* seconds 1000))))

(defn now []
  (js/Date.))

(defn current-time-vector []
  (date->vector (now)))
