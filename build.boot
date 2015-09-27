(set-env!
  :source-paths #{"markdown" "sass" "src"}

  :resource-paths #{"html"}

  :dependencies '[[org.clojure/clojurescript "1.7.48"]
                  [quiescent                 "0.2.0-RC2"]
                  [adzerk/boot-cljs          "1.7.48-4"]
                  [adzerk/boot-cljs-repl     "0.1.10-SNAPSHOT"]
                  [adzerk/boot-reload        "0.3.2"]
                  [pandeiro/boot-http        "0.6.3"]
                  [mathias/boot-sassc        "0.1.5"]
                  [secretary                 "1.2.3"]
                  [markdown-clj              "0.9.74"]])

(require
  '[adzerk.boot-cljs            :refer :all]
  '[adzerk.boot-cljs-repl       :refer :all]
  '[adzerk.boot-reload          :refer :all]
  '[pandeiro.boot-http          :refer :all]
  '[mathias.boot-sassc          :refer :all]
  '[big-time.tasks              :refer :all])

(task-options!
  sass {:sass-file "main.scss"
        :output-to "main.css"})

(deftask dev
  "Build cljs example for development."
  []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (markdown)
        (sass)
        (cljs)))

(deftask deploy
  "Deploy to divshot"
  []
  (comp (sass :output-style "compressed")
        (cljs :optimizations :advanced)
        (with-post-wrap fileset
          (dosh "divshot" "push" "production"))))
