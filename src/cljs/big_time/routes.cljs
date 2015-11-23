(ns big-time.routes
  (:require [bidi.bidi :as bidi]))

(def routes
  ["/" {"" :time
        "countdown" :countdown
        "about" :about}])

(defn path->component [path]
  (:handler (bidi/match-route routes path)))

(defn component->path [component]
  (str "/#" (bidi/path-for routes component)))
