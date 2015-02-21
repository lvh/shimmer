(defproject shimmer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.logic "0.8.8"]

                 ;; HTTP magic
                 [aleph "0.4.0-beta3"]
                 [ring/ring-mock "0.2.0"]]
  :main ^:skip-aot shimmer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
