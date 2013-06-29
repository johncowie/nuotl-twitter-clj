(ns nuotl-twitter.responder-test
  (:require [nuotl-twitter.responder :as r]
            [nuotl-twitter.messages :as m]
            [clj-time.core :as t])
  (:use [midje.sweet])
  )

(defn test-reply-fn [tweet-id message]
  {tweet-id message})

(defn create-tweet [tweet-id user-name user-id]
  {:_id tweet-id  :tweeter {:_id user-id :name user-name}})

(facts
 (against-background
  (m/get-message :success) => "SUCCESS"
  (m/get-message :area-error) => "AREA-ERROR")
 (r/respond test-reply-fn (create-tweet 77 "derek" 44) :success (t/date-time 2013 3))
 => {77 "@derek SUCCESS http://nextupontheleft.org/events/2013/3#77"}
 (r/respond test-reply-fn (create-tweet 77 "derek" 44) :area-error nil)
 => {77 "@derek AREA-ERROR http://twitter.com/44/status/77"}
 )
