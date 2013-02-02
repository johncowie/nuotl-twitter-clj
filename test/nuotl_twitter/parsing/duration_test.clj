(ns nuotl-twitter.parsing.duration-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.duration :as dur]
            ))

(facts
 (dur/parse-duration "adsfia") => (throws Exception ":duration-error")
 (dur/parse-duration "1m") => 1
 (dur/parse-duration "3h") => 180
 (dur/parse-duration "0.5d") => 720
 (dur/parse-duration "-1m") => (throws Exception ":duration-error")
 (dur/parse-duration "TTTd") => (throws Exception ":duration-error")
 (dur/parse-duration "0.6m") => 1
 (dur/parse-duration "1.23d") => 1772
 (dur/parse-duration "0m") => (throws Exception ":duration-error")
 (dur/parse-duration "0.1m") => 1
 (dur/parse-duration "2H") => 120
 (dur/parse-duration "0.1D") => 144
 (dur/parse-duration "14M") => 14
 )
