(ns nuotl-twitter.parsing.tweet-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.tweet :as p])
  )

(future-facts
 (p/parse-tweet "@nuotl 01/02/2013 08:00 3h N Hello World!") => nil
 )
