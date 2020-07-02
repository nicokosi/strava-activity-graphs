(ns strava-activity-graphs.core
  (:gen-class)
  (:use [incanter.charts]
        [incanter.core]
        [incanter.io])
  (:require [clojure.data.json :as json]
            [clj-http.client :as http-client]))

(defn strava-json-activities [token]
  (json/read-str (:body
                   (http-client/get
                     "https://www.strava.com/api/v3/activities"
                     {
                      :headers {:Authorization (str "Bearer " token)}
                      :query-params {:per_page 200}}))))

(def meters-per-second->kilometers-per-hour (partial * 3.6))
(defn- seconds->minutes [s] (/ s 60))
(defn- string-date->millis [str-date]
  (.getTime
    (clojure.instant/read-instant-date str-date)))

(defn- get-activities [token]
  (->> (strava-json-activities token)
       (map #(update-in % ["average_speed"] meters-per-second->kilometers-per-hour))
       (map #(update-in % ["start_date_local"] string-date->millis))
       (map #(update-in % ["elapsed_time"] seconds->minutes))
       (map #(update-in % ["moving_time"] seconds->minutes))))

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

(defn display-charts [token]
  (let [activities
        (dataset
            ["start_date_local" "average_speed" "kudos_count" "type" "pr_count" "elapsed_time"]
            (get-activities token))]
    (save activities "/tmp/strava-activities.csv"
          :delim \;)
    (with-data activities
      #_(view $data)
      (let [start_date_local (sel activities :cols 0)
            average_speed (sel activities :cols 1)
            kudos_count (sel activities :cols 2)
            type (sel activities :cols 3)
            pr_count (sel activities :cols 4)
            elapsed_time (sel activities :cols 5)]
      (ts-plot
        start_date_local
        average_speed
        :group-by type
        :title "Average speed over time"
        :x-label "time"
        :y-label "average speed (km/h)")
      (ts-plot
        start_date_local
        kudos_count
        :group-by type
        :title "Activity kudos over time"
        :x-label "time"
        :y-label "kudos")
      (ts-plot
        start_date_local
        pr_count
        :group-by type
        :title "Personal records over time"
        :x-label "time"
        :y-label "personal records")
      (ts-plot
        start_date_local
        elapsed_time
        :group-by type
        :title "Activity duration over time"
        :x-label "time"
        :y-label "activity's elapsed time (minutes)")))))

(defn -main
  [& args]
  (if (empty? args)
    (System/exit -1)
    (println "Strava API token provided"))
  (display-charts (first args)))