(ns big-time.routes
  (:require [big-time.ui.clock :as clock]
            [big-time.ui.countdown :as countdown]
            [big-time.ui.pages :as pages]))

(def routes
  ["/" {"" clock/Clock
        "about" pages/About}])
