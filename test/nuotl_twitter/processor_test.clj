(ns nuotl-twitter.processor-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.processor :as p]
            [nuotl-twitter.dao :as dao]
            [clj-time.core :as t]
            ))


(defn create-tweeter [tweeter_id]
  {:_id tweeter_id :name "Name" :display-name "Display Name"})

(defn create-tweet [text tweeter_id]
  {:_id 1234 :text text
   :tweeter (create-tweeter tweeter_id)
   :hashtags ["environment" "cuts"]
   :urls [{:url "http://ty.co/asdasd" :display-url "bbc.co.uk"
           :expanded-url "http://bbc.co.uk" :start 44 :end 54}]
   :in-response-to 2345
   })


(facts
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "Y")
  (dao/get-area-ids) => '("n")
  (dao/add-tweeter (create-tweeter 22) true) => anything
  (dao/add-event {:_id 1234
                  :text "Hello World"
                  :start (t/date-time 2013 1 25 6 0 0)
                  :end (t/date-time 2013 1 25 9 0 0)
                  :area :n
                  :tweeter 22
                  :hashtags ["environment" "cuts"]
                  }) => anything)
 (p/process-tweet (create-tweet "@nuotl 25/1/2013 6am 3h N Hello World" 22)) => nil ;success
 )

(facts
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "N")
  (dao/add-tweeter (create-tweeter 22) false) => anything)
 (p/process-tweet (create-tweet "@nuotl blah blah blah" 22))
      => (throws Exception (str :unapproved))
 )

(facts
 (p/set-listener-id! 99)
 (p/process-tweet (create-tweet "@nuotl blah blah blah" 99)) => (throws Exception (str :is-me))
 (provided (dao/add-reply-id 1234 2345) => {:_id 1234 :event-id 2345})
 )

(facts
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "Y")
  (dao/add-tweeter (create-tweeter 22) true) => anything)
 (p/process-tweet (create-tweet "@nuotl blah blah blah blah blah blahh" 22)) => (throws Exception (str :date-error))
 )

(facts
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "Y")
  (dao/add-tweeter (create-tweeter 22) true) => anything
  (dao/get-area-ids) => '("n"))
 (p/process-tweet (create-tweet "@nuotl 25/1/2013 6am 3h X Hello World" 22)) => (throws Exception (str :area-error))
 )
