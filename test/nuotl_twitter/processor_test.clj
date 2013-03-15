(ns nuotl-twitter.processor-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.processor :as p]
            [nuotl-twitter.dao :as dao]
            [nuotl-twitter.test-utils :refer [exception-with-code]]
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
  (t/now) => (t/date-time 2013 1 1)
  (dao/add-event {:_id 1234
                  :text "Hello World"
                  :start (t/date-time 2013 1 25 6 0 0)
                  :end (t/date-time 2013 1 25 9 0 0)
                  :area :n
                  :tweeter 22
                  :hashtags ["environment" "cuts"]
                  }) => anything)
 (p/process-tweet (create-tweet "@nuotl 25/1/2013 6am 3h N Hello World" 22))
 =>  {:_id 1234
      :text "Hello World"
      :start (t/date-time 2013 1 25 6 0 0)
      :end (t/date-time 2013 1 25 9 0 0)
      :area :n
      :tweeter 22
      :hashtags ["environment" "cuts"]
      })

(facts
 "Given an unauthorised tweeter
  When their tweet is processed
  Then the tweeter is processed
  And an unapproved exception is thrown"
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "N")
  (dao/add-tweeter (create-tweeter 22) false) => anything)
 (p/process-tweet (create-tweet "@nuotl blah blah blah" 22))
 => (throws (exception-with-code :unapproved))
 )

(facts
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "Y")
  (dao/add-tweeter (create-tweeter 22) true) => anything)
 (p/process-tweet (create-tweet "@nuotl blah blah blah blah blah blahh" 22))
 => (throws (exception-with-code :date-error))
 )

(facts
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "Y")
  (dao/add-tweeter (create-tweeter 22) true) => anything
  (dao/get-area-ids) => '("n"))
 (p/process-tweet (create-tweet "@nuotl 25/1/2013 6am 3h X Hello World" 22))
 => (throws (exception-with-code :area-error))
 )

(facts
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "Y")
  (dao/get-area-ids) => '("n")
  (dao/add-tweeter (create-tweeter 22) true) => anything
  (t/now) => (t/date-time 2013 1 1 10 0 0)
  (dao/add-event anything) => anything)
 (p/process-tweet (create-tweet "@nuotl 1/1/2013 9am 1h N Hello World" 22)) =>
 (throws (exception-with-code :in-past-error))
 )

(facts
 (against-background
  (dao/get-tweeter 22) => (assoc (create-tweeter 22) :approved "Y")
  (dao/get-area-ids) => '("n")
  (dao/add-tweeter (create-tweeter 22) true) => anything
  (t/now) => (t/date-time 2013 1 1 10 0 0)
  )
 (p/process-tweet (assoc (create-tweet  "@nuotl 25/1/2013 6am 3h N Hello World" 22) :urls []))
 => (throws (exception-with-code :no-url))
 )
