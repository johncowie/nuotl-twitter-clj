(ns nuotl-twitter.responder-test
  (:require [nuotl-twitter.responder :as r]
            [nuotl-twitter.messages :as m]
            [clj-time.core :as t])
  (:use [midje.sweet])
  )

(defn test-reply-fn [tweet-id message]
  {tweet-id message})

(defn create-tweet [tweet-id user-name user-id]
  {:_id tweet-id :start (t/date-time 2013 3 2) :tweeter {:_id user-id :name user-name}})

(facts
 (against-background (m/get-message :success) => "SUCCESS")
 (r/respond test-reply-fn (create-tweet 77 "derek" 44) :success)
 => {77 "@derek SUCCESS http://nextupontheleft.org/events/3/2013#77"}
 (r/respond test-reply-fn (create-tweet 89 "bobgeldof" 61) :is-me)
 => nil
 )
