(ns nuotl-twitter.parsing.tweet-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.tweet :as p]
            [clj-time.core :as t]
            [clj-time.local :as l]
            [nuotl-twitter.dao :as dao]))

(facts
 (against-background (dao/get-area-ids) => '("n" "cf")
                     (l/local-now) => (t/date-time 2013 1 1 2 0 0))
 (p/parse-tweet "@nuotl") => {:error :too-short-error :event nil}
 (p/parse-tweet "@nuotl X X X X TEXT") => {:error :date-error :event nil}
 (p/parse-tweet "@nuotl 1/2/2013 X X X TEXT") =>  {:error :time-error :event nil}
 (p/parse-tweet "@nuotl 1/2/2013 8am X X TEXT") => {:error :duration-error :event nil}
 (p/parse-tweet "@nuotl 1/2/2013 8am 3h X TEXT") => {:error :area-error :event nil}
 (p/parse-tweet "@nuotl 1/2/2013 8am 3h N TEXT") => {:error nil
                                                     :event {:start (t/date-time 2013 2 1 8 0 0)
                                                       :end (t/date-time 2013 2 1 11 0 0)
                                                       :area :n
                                                       :text "TEXT"}}
 (p/parse-tweet "@NUOTL 20/4/2013 8AM 3H N TEXT") => {:error nil
                                                      :event {:start (t/date-time 2013 4 20 8 0 0)
                                                        :end (t/date-time 2013 4 20 11 0 0)
                                                        :area :n
                                                        :text "TEXT"}}
 (p/parse-tweet "@nuotl 20/4/2013 15PM 8H CF TEXT") => {:error nil
                                                        :event {:start (t/date-time 2013 4 20 15 0 0)
                                                          :end (t/date-time 2013 4 20 23 0 0)
                                                          :area :cf
                                                          :text "TEXT"}}
 (p/parse-tweet "@nuotl 1/1/2013 1am 2H N TEXT") => {:error :in-past-error
                                                        :event nil}
 )
