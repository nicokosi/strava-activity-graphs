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
         :per_page     200}})
      :body)))

(defn- ts-plot [x y & options]
  (doto
    (apply time-series-plot*
           x y
           :points true
           :legend true
           options)
    (set-point-size 4)
    (set-stroke-color java.awt.Color/GRAY)
    (set-alpha 0.8)
    view))

(def meters-per-hour->kilometers-per-hour (partial * 3.6))
(defn- seconds->minutes [s] (/ s 60))
(defn- string-date->millis [str-date]
  (.getTime
    (clojure.instant/read-instant-date str-date)))

(defn -main
  [& args]

  (if (empty? args)
    (System/exit -1)
    (println "Strava API token provided"))
  (let
    [token (first args)
     activities (->> (strava-json-activities token)
                     (map #(update-in % ["average_speed"] meters-per-hour->kilometers-per-hour))
                     (map #(update-in % ["start_date_local"] string-date->millis))
                     (map #(update-in % ["elapsed_time"] seconds->minutes))
                     (map #(update-in % ["moving_time"] seconds->minutes)))]

    (with-data
      (to-dataset activities)
      #_(view $data)
      (ts-plot
        ($ :start_date_local)
        ($ :average_speed)
        :group-by ($ :type)
        :title "Average speed over time"
        :x-label "time"
        :y-label "average speed (km/h)")
      (ts-plot
        ($ :start_date_local)
        ($ :kudos_count)
        :group-by ($ :type)
        :title "Activity kudos over time"
        :x-label "time"
        :y-label "kudos")
      (ts-plot
        ($ :start_date_local)
        ($ :pr_count)
        :group-by ($ :type)
        :title "Personal records over time"
        :x-label "time"
        :y-label "personal records")
      (ts-plot
        ($ :start_date_local)
        ($ :elapsed_time)
        :group-by ($ :type)
        :title "Activity duration over time"
        :x-label "time"
        :y-label "activity's elapsed time (minutes)"))
    ))