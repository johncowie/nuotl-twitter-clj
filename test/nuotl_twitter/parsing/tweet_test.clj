(ns nuotl-twitter.parsing.tweet-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.tweet :as p]
            [clj-time.core :as t]
            [nuotl-twitter.dao :as dao]
            [nuotl-twitter.test-utils :refer [exception-with-code]]))

(facts
 (against-background (dao/get-area-ids) => '("n" "cf")
                     (t/now) => (t/date-time 2013 1 1))
 (p/parse-tweet "@nuotl") => (throws (exception-with-code :too-short-error))
 (p/parse-tweet "@nuotl X X X X TEXT") => (throws (exception-with-code :date-error))
 (p/parse-tweet "@nuotl 1/2/2013 X X X TEXT") => (throws (exception-with-code :time-error))
 (p/parse-tweet "@nuotl 1/2/2013 8am X X TEXT") => (throws (exception-with-code :duration-error))
 (p/parse-tweet "@nuotl 1/2/2013 8am 3h X TEXT") => (throws (exception-with-code :area-error))
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
                                                        :text "TEXT"}
 (p/parse-tweet "@nuotl 31/12/2012 11PM 2H N TEXT") => (throws (exception-with-code :in-past-error))
 )
