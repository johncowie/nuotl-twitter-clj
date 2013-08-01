(ns nuotl-twitter.integration-test
  (:require [midje.sweet :refer [facts against-background => anything]]
            [nuotl-twitter.core :refer [listener]]
            [nuotl-twitter.dao :as dao]
            [clj-time.core :as t]
            [clj-time.local :as l])
  (:import [org.nextupontheleft.twitter
            TestUser TestHashtagEntity TestUrlEntity TestStatus MockTwitter]))

(defn user [id display-name name]
  (TestUser. id display-name name))

(defn hashtag [text]
  (TestHashtagEntity. text))

(defn url-entity [url expanded-url display-url]
  (TestUrlEntity. url expanded-url display-url))

(defn status [id text in-reply-to-status user hashtag-entities url-entities]
  (TestStatus. id text in-reply-to-status user hashtag-entities url-entities))

(def twitter (MockTwitter. 20 "nuotl"))

(defn handle-status [status]
  (. (listener twitter) (onStatus status))
  (. twitter (getLastUpdate))
  )

(facts
 (against-background (l/local-now) => (t/date-time 2013 1 1))
 (handle-status (status 1 "@nuotl 1/4/2013 08:00 4h CF An event http://ty.co" 2
                        (user 3 "J C" "jc")
                        (url-entity "http://ty.co" "http://bbc.co.uk" "bbc.co.uk")
                        (hashtag "hashtag")
                        ))
  => "@jc Your event tweet was successful! http://nextupontheleft.org/events/2013/4#1"
 (provided
  (dao/tweeter-approved? 3) => true
  (dao/add-or-update-tweeter {:_id 3 :name "jc" :display-name "J C"}) => nil
  (dao/get-area-ids) => '("n" "cf")
  (dao/add-event {
                  :_id 1
                  :start (t/date-time 2013 4 1 8 0 0)
                  :end (t/date-time 2013 4 1 12 0 0)
                  :area :cf
                  :tweeter 3
                  :tags ["hashtag"]
                  :in-response-to 2
                  :text "An event <a href=\"http://bbc.co.uk\">bbc.co.uk</a>"
                  }) => nil))
