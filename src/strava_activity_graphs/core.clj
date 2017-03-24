(ns strava-activity-graphs.core
  (:gen-class)
  (:use [incanter.charts]
        [incanter.core])
  (:require [clojure.data.json :as json]
            [clj-http.client :as http-client]))

(defn- strava-json-activities [token]
  (json/read-str
    ((http-client/get
       "https://www.strava.com/api/v3/activities"
       {:query-params
        {:access_token token
         :per_page 200}})
      :body)))

(defn- to-millis [str-date]
  (.getTime
    (clojure.instant/read-instant-date str-date)))

(defn- ts-plot [x y & options]
  (doto
    (apply time-series-plot* x y :points true options #_(conj :points true options))
    (set-point-size 4)
    (set-stroke-color java.awt.Color/GRAY)
    (set-alpha 0.8)
    view))

(defn -main
  [& args]

  (if (empty? args)
    (System/exit -1)
    (println "Strava API token provided"))
  (let
    [token (first args)
     activities (strava-json-activities token)
     to-millis (fn [dates] (map #(to-millis %) dates))]

    (with-data
      (to-dataset activities)
      #_(view $data)
      (ts-plot
        (to-millis ($ :start_date_local))
        ($ :average_speed)
        :title "Average speed over time"
        :x-label "time"
        :y-label "average speed (m/s)")
      (ts-plot
        (to-millis ($ :start_date_local))
        ($ :average_speed)
        :title "Average speed over time"
        :x-label "time"
        :y-label "average speed (m/s)")
      (ts-plot
        (to-millis ($ :start_date_local))
        ($ :kudos_count)
        :title "Activity kudos over time"
        :x-label "time"
        :y-label "kudos")
      (ts-plot
        (to-millis ($ :start_date_local))
        ($ :pr_count)
        :title "Personal records over time"
        :x-label "time"
        :y-label "personal records")
      (ts-plot
        (to-millis ($ :start_date_local))
        ($ :elapsed_time)
        :title "Activity duration over time"
        :x-label "time"
        :y-label "activity's elapsed time (s)"))))