(ns big-time.routes
  (:require [bidi.bidi :as bidi]
            [big-time.ui.clock :as clock]
            [big-time.ui.countdown :as countdown]
            [big-time.ui.pages :as pages]))

(def routes
  {:clock ["" clock/Clock]
   :countdown ["countdown" countdown/Countdown]
   :about ["about" pages/About]})

(def bidi-routes
  ["/" (into {} (vals routes))])

(defn path->component [path]
  (:handler (bidi/match-route bidi-routes path)))

(defn component->path [component-key]
  (let [[path component] (component-key routes)]
    (str "/#" (bidi/path-for bidi-routes component))))
