(defproject strava-activity-graphs "0.1.0-SNAPSHOT"
  :description "Generate statistical charts for Strava activities"
  :url "https://github.com/nicokosi/strava-activity-graphs"
  :license {:name "Creative Commons Attribution 4.0"
            :url  "https://creativecommons.org/licenses/by/4.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [incanter/incanter-core "1.9.3"]
                 [incanter/incanter-charts "1.9.3"]
                 [incanter/incanter-io "1.9.3"]
                 [org.clojure/data.json "2.3.0"]
                 [clj-http "3.12.1"]
                 [slingshot "0.12.2"]]
  :plugins [[lein-cljfmt "0.8.0"]]
  :main ^:skip-aot strava-activity-graphs.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
