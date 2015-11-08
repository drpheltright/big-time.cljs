(ns big-time.core
  (:require [goog.events :as events]
            [secretary.core :as secretary]
            [big-time.ui.app :as app]
            [big-time.ui.clock :as clock]
            [big-time.ui.pages :as pages])
  (:import [goog History]
           [goog.history EventType]))

(enable-console-print!)

(def data-atom (atom {:backgrounds [:#79BD9A :#3B8686 :#0B486B]
                      :current-time nil
                      :page-component nil
                      :path nil}))

(add-watch data-atom :path-logger
  (fn [key data-atom state next-state]
    (println next-state)))

(add-watch data-atom :path-dispatcher
  (fn [key data-atom state next-state]
    (if (not= (:path state) (:path next-state))
      (secretary/dispatch! (:path next-state)))))

(add-watch data-atom :re-renderer
  (fn [key data-atom state next-state]
    (if-let [component (:page-component next-state)]
      (app/render component data-atom))))

(secretary/set-config! :prefix "#")

(secretary/defroute clock-path "/" []
  (swap! data-atom assoc :page-component clock/Clock))

(secretary/defroute about-path "/about" []
  (swap! data-atom assoc :page-component pages/About))

(let [history (History.)]
  (events/listen history EventType.NAVIGATE
    (fn [e]
      (let [token (.-token e)]
        (swap! data-atom assoc :path (if (empty? token) "/" token)))))

  (.setEnabled history true))
