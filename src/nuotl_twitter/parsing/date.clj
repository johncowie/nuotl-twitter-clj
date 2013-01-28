(ns nuotl-twitter.parsing.date
  (:require [clj-time.format :as f]
            [clj-time.core :as t]
            [clojure.string :as s]
            ))

(defn today [] (t/now))

(defn tomorrow []
  (t/plus (today) (t/days 1)))

(defn day-id {
              "monday" 1
              "tuesday" 2
              "wednesday" 3
              "thursday" 4
              "friday" 5
              "saturday" 6
              "sunday" 7
              })

(defn parse-date [val]
  (case (s/lower-case val)
    ("today" "2day") (today)
    ("tomorrow" "2moro" "2morro" "tomoro" "2morrow" "2morow") (tomorrow)
    (try
      (f/parse (f/formatter "dd/MM/yyyy") val)
      (catch Exception e nil))))
