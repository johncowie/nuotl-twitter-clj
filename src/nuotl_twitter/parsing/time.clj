(ns nuotl-twitter.parsing.time
  (:require [clj-time.core :as t]
            [clj-time.coerce :as coerce])
  (:import  [java.util Calendar]
            [com.mdimension.jchronic Chronic]))

(defn parse-time [string]
  (if-let [span  (Chronic/parse string)]
    (let [cal (. span (getBeginCalendar))]
      (t/date-time 1 1 1
                   (. cal (get (Calendar/HOUR_OF_DAY)))
                   (. cal (get (Calendar/MINUTE)))
                   (. cal (get (Calendar/SECOND)))))
    nil))
