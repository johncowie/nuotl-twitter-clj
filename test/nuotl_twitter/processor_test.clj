(ns nuotl-twitter.processor-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.processor :as p]
            [nuotl-twitter.dao :as dao]
            ))


(def example {:_id 1234 :text "@nuotl 1/2/2013 08:00 3h N Some sort of event bbc.co.uk"
              :tweeter {:_id 25 :name "johncowiedev" :display-name "John Cowie"}
              :hashtags ["environment" "cuts"]
              :urls [{:url "http://ty.co/asdasd" :display-url "bbc.co.uk"
                      :expanded-url "http://bbc.co.uk" :start 44 :end 54}]})

(future-facts
 (p/process-tweet example 77) => :success
 (provided (dao/add-tweeter {} true) => nil)
 )
