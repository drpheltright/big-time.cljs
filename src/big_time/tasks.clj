(ns big-time.tasks
  (:require [boot.core :as b]))

(b/deftask markdown
  "Touch markdown.clj if .md files change"
  []
  (let [prev (atom nil)]
    (b/with-pre-wrap [fileset]
      (let [changed (b/fileset-diff @prev fileset)
            changed-paths (map #(:path %) (b/input-files changed))
            md-paths (filter #(.endsWith % ".md") changed-paths)]
        (when-not (empty? md-paths)
          (println "Markdown changed:" md-paths)
          (spit "src/big_time/markdown.clj" "" :append true))
        (reset! prev fileset)))))
