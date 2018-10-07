(ns strava-activity-graphs.core-test
  (:require
    [strava-activity-graphs.core :refer :all]
    [clojure.java.io :refer [resource]]
    [clojure.test :refer [deftest is testing]]
    [clojure.data.json :as json]))

(deftest ^:integration-test core-tests

    (testing "Display charts should not fail"
       (with-redefs
          [strava-json-activities
           (fn [_]
             (json/read-str
               (slurp (resource "strava_activity_graphs/strava-activities.json"))))]

          (display-charts "fake-token")))

    (testing "Display charts should not fail with even more recent data"
        (with-redefs
            [strava-json-activities
              (fn [_]
                (json/read-str
                  (slurp (resource "strava_activity_graphs/strava-activities-2.json"))))]

          (display-charts "fake-token")))

)