(ns nuotl-twitter.responder-test
  (:require [nuotl-twitter.responder :as r]
            [nuotl-twitter.messages :as m])
  (:use [midje.sweet])
  )

(defn reply-fn [tweet-id message]
  {tweet-id message})

(defn create-tweet [tweet-id user-name user-id]
  {:_id tweet-id :tweeter {:_id user-id :name user-name}})

(facts
 (against-background (m/get-message :success) => "SUCCESS")
 (r/respond reply-fn (create-tweet 77 "derek" 44) :success)
 => {77 "@derek SUCCESS http://twitter.com/44/status/77"}
 (r/respond reply-fn (create-tweet 89 "bobgeldof" 61) :from-listener-so-ignored)
 => nil
 )
