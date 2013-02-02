(ns nuotl-twitter.parsing.time
  (:require [clj-time.core :as t]
            [clj-time.coerce :as coerce]))

(defn parse-time [string]
  (if-let [span  (com.mdimension.jchronic.Chronic/parse string)]
    (coerce/from-long (.. span (getBeginCalendar) (getTime) (getTime)))
    (throw (Exception. (str :time-error)))))
