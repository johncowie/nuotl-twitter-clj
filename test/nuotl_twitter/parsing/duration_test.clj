(ns nuotl-twitter.parsing.duration-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.duration :as dur])
  )

(facts
 (dur/parse-duration "adsfia") => nil
 (dur/parse-duration "1m") => 1
 (dur/parse-duration "3h") => 180
 (dur/parse-duration "0.5d") => 720
 (dur/parse-duration "-1m") => nil
 (dur/parse-duration "TTTd") => nil
 (dur/parse-duration "0.6m") => 1
 (dur/parse-duration "1.23d") => 1772
 (dur/parse-duration "0m") => nil
 (dur/parse-duration "0.1m") => 1
 )
