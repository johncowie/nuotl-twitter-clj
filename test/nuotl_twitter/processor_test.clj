(ns nuotl-twitter.processor-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.processor :as p]
            [nuotl-twitter.dao :as dao]
            [nuotl-twitter.parsing.tweet :as parser]
            [clj-time.core :as t]
            ))

(defn create-tweeter [tweeter_id]
  {:_id tweeter_id :name "Name" :display-name "Display Name"})

(defn create-tweet [text tweeter_id]
  {:_id 1234 :text text
   :tweeter (create-tweeter tweeter_id)
   :tags ["environment" "cuts"]
   :urls [{:url "http://ty.co" :display-url "bbc.co.uk"
           :expanded-url "http://bbc.co.uk" :start 44 :end 54}]
   :in-response-to 2345
   :application-id 1
   :application-screen-name "nuotl"
   })

(facts
 (against-background
  (dao/tweeter-approved? 22) => true
  (parser/parse-tweet anything) => {:event {:start (t/date-time 2013 1 25 6 0 0)
                                            :end (t/date-time 2013 1 25 9 0 0)
                                            :area :n
                                            :text "Hello World http://ty.co"
                                            }
                                     :error nil})
 (let [test-tweet (create-tweet "@nuotl 25/1/2013 6am 3h N Hello World http://ty.co" 22)]
   (p/process-tweet (create-tweet "@nuotl 25/1/2013 6am 3h N Hello World http://ty.co" 22))
   =>  {:event {:_id 1234
                :text "Hello World <a href=\"http://bbc.co.uk\">bbc.co.uk</a>"
                :start (t/date-time 2013 1 25 6 0 0)
                :end (t/date-time 2013 1 25 9 0 0)
                :area :n
                :tweeter 22
                :in-response-to 2345
                :tags ["environment" "cuts"]
                }
        :tweeter {:_id 22
                  :name "Name"
                  :display-name "Display Name"
                  }
        :message :success
        }))

(facts
 "Given a tweet from the NUOTL account (id 1)
  When the tweet is processed
  Then the processing should stop
  And no information should be returned"
 (p/process-tweet (create-tweet "A response" 1))
 => {:event nil :tweeter nil :message nil}
 )

(facts
 "Given a tweet that doesn't mention the app in the first token
  When the tweet is processed
  Then the processing should stop
  And no information should be returned"
 (p/process-tweet (create-tweet "@bob blah blah blah blah blah" 22))
 => {:event nil :tweeter nil :message nil}
 )


(facts
 "Given an unauthorised tweeter
  When their tweet is processed
  Then the tweeter is processed
  And an unapproved exception is thrown"
 (against-background
  (dao/tweeter-approved? 22) => false)
 (p/process-tweet (create-tweet "@nuotl blah blah blah" 22))
 => {:event nil :tweeter (create-tweeter 22) :message :unapproved}
 )

(facts
 (against-background
  (dao/tweeter-approved? 22) => true
  (parser/parse-tweet anything) => {:event nil :error :some-error}
  )
 (p/process-tweet (create-tweet "@nuotl blah blah blah" 22))
 => {:event nil :tweeter (create-tweeter 22) :message :some-error}
 )

(facts
 (against-background
  (dao/tweeter-approved? 22) => true
  (parser/parse-tweet anything) => {:event {} :error nil}
  )
 (p/process-tweet (assoc (create-tweet  "@nuotl 25/1/2013 6am 3h N Hello World" 22) :urls []))
 => {:event nil :tweeter (create-tweeter 22) :message :no-url}
 )
