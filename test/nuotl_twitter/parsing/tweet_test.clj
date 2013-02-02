(ns nuotl-twitter.parsing.tweet-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.tweet :as p]
            [clj-time.core :as t]
            [nuotl-twitter.dao :as dao]))

(facts
 (against-background (dao/get-area-ids) => '("n" "cf"))
 (p/parse-tweet "@nuotl") => (throws Exception ":too-short-error")
 (p/parse-tweet "@nuotl X X X X TEXT") => (throws Exception ":date-error")
 (p/parse-tweet "@nuotl 1/2/2013 X X X TEXT") => (throws Exception ":time-error")
 (p/parse-tweet "@nuotl 1/2/2013 8am X X TEXT") => (throws Exception ":duration-error")
 (p/parse-tweet "@nuotl 1/2/2013 8am 3h X TEXT") => (throws Exception ":area-error")
 (p/parse-tweet "@nuotl 1/2/2013 8am 3h N TEXT") => {:start (t/date-time 2013 2 1 8 0 0)
                                                     :end (t/date-time 2013 2 1 11 0 0)
                                                     :area :n
                                                     :text "TEXT"}
 (p/parse-tweet "@NUOTL 20/4/2013 8AM 3H N TEXT") => {:start (t/date-time 2013 4 20 8 0 0)
                                                      :end (t/date-time 2013 4 20 11 0 0)
                                                      :area :n
                                                      :text "TEXT"}
 (p/parse-tweet "@nuotl 20/4/2013 15PM 8H CF TEXT") => {:start (t/date-time 2013 4 20 15 0 0)
                                                        :end (t/date-time 2013 4 20 23 0 0)
                                                        :area :cf
                                                        :text "TEXT"
                                                        }
 )
