(ns nuotl-twitter.responder
  (:require [nuotl-twitter.messages :as m]
            [clj-time.core :as t]))

(defn- tweet-url [tweet code start-date]
  (if (= code :success)
    (let [year (t/year start-date) month (t/month start-date)]
      (format "http://nextupontheleft.org/events/%s/%s#%s" year month (tweet :_id)))
    (format "http://twitter.com/%s/status/%s" ((tweet :tweeter) :_id) (tweet :_id))))

(defn respond [replyfn tweet code start-date]
  (println "CODE: " code)
  (if-not (or (nil? code) (= code :is-me))
    (let [name (:name (:tweeter tweet))
          tweet-id (:_id tweet)
          text (m/get-message code)
          url (tweet-url tweet code start-date)]
      (replyfn tweet-id
               (format "@%s %s %s" name text url)))))
