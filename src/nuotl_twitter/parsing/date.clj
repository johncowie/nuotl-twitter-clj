(ns nuotl-twitter.parsing.date
  (:require [clj-time.format :as f]))

(defn parse-date [string]
  (try
    (f/parse (f/formatter "dd/MM/yyyy") string)
    (catch Exception e nil)))
