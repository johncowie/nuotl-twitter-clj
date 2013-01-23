(ns nuotl-twitter.parsing.tweet-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.tweet :as p]
            [clj-time.core :as t]))

(facts
 (p/parse-tweet "@nuotl") => {:error :not-enough-words}
 (p/parse-tweet "@nuotl X X X X TEXT") => {:error :date}
 (p/parse-tweet "@nuotl 1/2/2013 X X X TEXT") => {:error :time}
 (p/parse-tweet "@nuotl 1/2/2013 8am X X TEXT") => {:error :duration}
 (p/parse-tweet "@nuotl 1/2/2013 8am 3h X TEXT") => {:error :area}
 (p/parse-tweet "@nuotl 1/2/2013 8am 3h N TEXT") => {:start (t/date-time 2013 2 1 8 0 0)
                                                     :end (t/date-time 2013 2 1 11 0 0)
                                                     :area :n
                                                     :text "TEXT"}
 )
