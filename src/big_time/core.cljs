(ns big-time.core
  (:require [goog.events :as events]
            [secretary.core :as secretary]
            [big-time.ui.app :as app]
            [big-time.ui.clock :as clock]
            [big-time.ui.pages :as pages])
  (:import [goog History]
           [goog.history EventType]))

(enable-console-print!)

; Default application state
;
(def data-atom (atom {:backgrounds [:#79BD9A :#3B8686 :#0B486B]
                      :current-time nil
                      :page-component nil
                      :path nil}))

; Just for logging application state changes
;
(add-watch data-atom :path-logger
  (fn [key data-atom state next-state]
    (println next-state)))

; Rebuilds app everytime state changes provided it can find a page
; component to render.
;
(add-watch data-atom :re-renderer
  (fn [key data-atom state next-state]
    (if-let [component (:page-component next-state)]
      (app/render component data-atom))))

; Everytime application state :path changes we dispatch secretary.
; Secretary will then update application state :page-component
; which represents main page component for that URL.
;
(add-watch data-atom :path-dispatcher
  (fn [key data-atom state next-state]
    (if (not= (:path state) (:path next-state))
      (secretary/dispatch! (:path next-state)))))

(secretary/set-config! :prefix "#")

(secretary/defroute clock-path "/" []
  (swap! data-atom assoc :page-component clock/Clock))

(secretary/defroute about-path "/about" []
  (swap! data-atom assoc :page-component pages/About))

; History will update application state :path everytime URL changes
;
(let [history (History.)]
(events/listen history EventType.NAVIGATE
(fn [e]
(let [token (.-token e)]
  (swap! data-atom assoc :path (if (empty? token) "/" token)))))

(.setEnabled history true))
