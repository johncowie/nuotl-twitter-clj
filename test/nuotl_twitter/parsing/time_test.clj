(ns nuotl-twitter.parsing.time-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.time :as parser]
            [clj-time.core :as t]
            ))

(defn matches-time? [h m s]
   (fn [date]
     (and (not (nil? date))
          (= (t/hour date) h)
          (= (t/minute date) m)
          (= (t/sec date) s))))

(facts
 (parser/parse-time "08:00") => (matches-time? 8 0 0)
 (parser/parse-time "blah") => (throws Exception ":time-error")
 (parser/parse-time "7am") => (matches-time? 7 0 0)
 (parser/parse-time "2100") => (matches-time? 21 0 0)
 (parser/parse-time "10pm") => (matches-time? 22 0 0)
 (parser/parse-time "11:23pm") => (matches-time? 23 23 0)
 (parser/parse-time "midnight") => (matches-time? 0 0 0)
 )
