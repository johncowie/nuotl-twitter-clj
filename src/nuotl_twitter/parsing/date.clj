(ns nuotl-twitter.parsing.date
  (:require [clj-time.format :as f]
            [clj-time.core :as t]
            [clojure.string :as s]
            ))

(defn- today [] (t/now))

(defn- tomorrow []
  (t/plus (today) (t/days 1)))

(def day-ids {
              "monday" 1
              "tuesday" 2
              "wednesday" 3
              "thursday" 4
              "friday" 5
              "saturday" 6
              "sunday" 7
              })

(defn- get-day-number [dstr]
  (if (< (count dstr) 2)
    nil
    (loop [i 0]
      (if (< i (count day-ids))
        (let [id (nth (keys day-ids) i)]
          (if (nil? (re-matches (re-pattern (str (s/lower-case dstr) ".*")) id))
            (recur (inc i))
            (day-ids id)
            ))
        nil))))

(defn- get-relative-date [number]
  (loop [t (tomorrow)]
    (if (= (t/day-of-week t) number)
      t
      (recur (t/plus t (t/days 1))))))

(defn- parse-day-name [daystr]
  (let [daynum (get-day-number daystr)]
    (if (nil? daynum)
      nil
      (get-relative-date daynum))))

(defn- parse-relative-word [val]
  (case (s/lower-case val)
    ("today" "2day") (today)
    ("tomorrow" "2moro" "2morro" "tomoro" "2morrow" "2morow") (tomorrow)
    nil
  ))

(defn- parse-date-with-format [val format]
  (try
    (f/parse (f/formatter format) val)
    (catch Exception e nil)))


(def parsers [
              parse-relative-word
              parse-day-name
              #(parse-date-with-format % "dd/MM/yyyy")
              #(parse-date-with-format % "dd.MM.yyyy")
              ])

(defn parse-date [val]
  (loop [i 0]
    (if (< i (count parsers))
      (let [d ((parsers i) val)]
        (if (nil? d)
          (recur (inc i))
          d
          ))
      nil)))
