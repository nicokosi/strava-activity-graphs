(defproject strava-activity-graphs "0.1.0-SNAPSHOT"
  :description "Generate statistical charts for Strava activities"
  :url "https://github.com/nicokosi/strava-activity-graphs"
  :license {:name "Creative Commons Attribution 4.0"
            :url  "https://creativecommons.org/licenses/by/4.0/"}
  :dependencies [
                 [org.clojure/clojure "1.8.0"],
                 [incanter "1.5.7"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-http "2.3.0"]
                 [slingshot "0.12.2"]
                 ]
  :main ^:skip-aot strava-activity-graphs.core
  :target-path "target/%s"
  :plugins [[lein-gorilla "0.4.0"]]
  :profiles {:uberjar {:aot :all}})
