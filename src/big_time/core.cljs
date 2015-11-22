(ns big-time.core
  (:require [goog.events :as events]
            [bidi.bidi :as bidi]
            [quiescent.core :as q]
            [goog.dom :as gdom]
            [big-time.ui.app :as app]
            [big-time.ui.time :as time]
            [big-time.ui.countdown :as countdown]
            [big-time.ui.pages :as pages]
            [big-time.routes :as routes]
            [big-time.worker :as worker])
  (:import [goog History]
           [goog.history EventType]))

(enable-console-print!)

(defn init []
  ; Default application state
  ;
  (def store (atom {:countdown {:form {:hours "00" :minutes "00" :seconds "10"}
                                :duration 0
                                :start-time nil
                                :current-time ["00" "00" "00"]}
                     :current-time ["00" "00" "00"]
                     :page nil
                     :pages {:time time/Time
                             :countdown countdown/Countdown
                             :about pages/About}
                     :path nil
                     :tasks {}}))

  (worker/create #(vals (:tasks @store)))

  ; Just for logging application state changes
  ;
  ; (add-watch store :path-logger
  ;   (fn [key store state next-state]
  ;     (println next-state)))

  ; Rebuilds app everytime state changes provided it can find a page
  ; component to render.
  ;
  (add-watch store :re-renderer
    (fn [key store state next-state]
      (if-let [page-component (:page next-state)]
        (do
          (println "rendering")
          (q/render (app/App @store page-component store) (gdom/getElement "app"))))))

  ; Everytime application state :path changes we dispatch secretary.
  ; Secretary will then update application state :page-component
  ; which represents main page component for that URL.
  ;
  (add-watch store :path-dispatcher
    (fn [key store state next-state]
      (if (not= (:path state) (:path next-state))
        (if-let [page-component (get (:pages next-state) (routes/path->component (:path next-state)))]
          (swap! store assoc :page page-component)))))

  ; History will update application state :path everytime URL changes
  ;
  (let [history (History.)]
    (events/listen history EventType.NAVIGATE
      (fn [e]
        (let [token (.-token e)]
          (swap! store assoc :path (if (empty? token) "/" token)))))

    (.setEnabled history true)))

(defn reload []
  (println "reloading")
  (swap! store assoc :dev (js/Date.))
  (swap! store assoc :path "/"))
