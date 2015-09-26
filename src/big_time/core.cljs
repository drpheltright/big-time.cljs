(ns big-time.core
  (:require [goog.events :as events]
            [secretary.core :as secretary]
            [big-time.ui.clock :as clock]
            [big-time.ui.pages :as pages])
  (:import [goog History]
           [goog.history EventType]))

(enable-console-print!)

(def data (atom {:backgrounds [:red :blue :green]
                 :current-time nil
                 :path nil}))

(secretary/set-config! :prefix "#")

(secretary/defroute clock-path "/" [] (clock/render data))
(secretary/defroute about-path "/about" [] (pages/render-about data))

(let [history (History.)]
  (events/listen history EventType.NAVIGATE
    (fn [e]
      (swap! data assoc :path (if (= (.-token e) "") "/" (.-token e)))
      (secretary/dispatch! (.-token e))))

  (.setEnabled history true))
