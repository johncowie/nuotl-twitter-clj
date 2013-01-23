(ns nuotl-twitter.parsing.area-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.area :as area]))

(facts
 (area/parse-area "asdf") => nil
 (area/parse-area "N") => :n
 (area/parse-area "n") => :n
 )
