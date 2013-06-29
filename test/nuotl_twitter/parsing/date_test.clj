(ns nuotl-twitter.parsing.date-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.date :as parser]
            [clj-time.core :as t]))

(defn matches-date? [y m d]
 (fn [val]
   (and (not (nil? val))
        (= (t/year val) y)
        (= (t/month val) m)
        (= (t/day val) d)
        )))

(facts
 (against-background
  (t/now) => (t/date-time 2013 1 28 7 0 0)
 )
 (parser/parse-date "lkjlsdf") => nil
 (parser/parse-date "25/12/2012") => (matches-date? 2012 12 25)
 (parser/parse-date "2/1/2013") => (matches-date? 2013 1 2)
 (parser/parse-date "today") => (matches-date? 2013 1 28)
 (parser/parse-date "tomorrow") => (matches-date? 2013 1 29)
 (parser/parse-date "monday") => (matches-date? 2013 2 4)
 (parser/parse-date "Tuesday") => (matches-date? 2013 1 29)
 (parser/parse-date "wed") => (matches-date? 2013 1 30)
 (parser/parse-date "t") => nil
 (parser/parse-date "SAT") => (matches-date? 2013 2 2)
 (parser/parse-date "25.12.2012") => (matches-date? 2012 12 25)
 )
