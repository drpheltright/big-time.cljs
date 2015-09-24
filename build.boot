(set-env!
  :source-paths #{"resources/sass" "src"}
  :resource-paths #{"resources"}
  :dependencies '[[org.clojure/clojurescript "1.7.48"]
                  [quiescent                 "0.2.0-RC2"]
                  [adzerk/boot-cljs          "1.7.48-4"]
                  [adzerk/boot-cljs-repl     "0.1.9"]
                  [adzerk/boot-reload        "0.3.2"]
                  [mathias/boot-sassc        "0.1.5"]])

(require
  '[adzerk.boot-cljs      :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload    :refer :all]
  '[mathias.boot-sassc    :refer :all])

(deftask dev
  "Build cljs example for development."
  []
  (comp (watch)
        (sass)
        (reload :on-jsload 'big-time.core/init)
        (cljs-repl)
        (cljs)))

(deftask deploy
  "Deploy to divshot"
  []
  (comp
    (sass :output-style "compressed")
    (cljs :optimizations :advanced)
    (with-post-wrap fileset
      (dosh "divshot" "push" "production"))))
