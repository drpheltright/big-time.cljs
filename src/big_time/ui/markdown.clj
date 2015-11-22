(ns big-time.ui.markdown
  (:require [markdown.core :refer [md-to-html-string]])
  (:import [java.io StringWriter]))

(defmacro component [file]
  (let [html (md-to-html-string (slurp file))]
    `(quiescent.dom/div {:className "markdown"
                         :dangerouslySetInnerHTML {:__html ~html}})))
