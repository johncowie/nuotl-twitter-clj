(ns nuotl-twitter.responder
  (:require [nuotl-twitter.messages :as m]
            [clj-time.core :as t]))

(defn- tweet-url [tweet code]
  (if (= code :success)
    (let [month (t/month (tweet :start))
          year (t/year (tweet :start))]
      (format "http://nextupontheleft.org/events/%s/%s#%s" year month (tweet :_id)))
    (format "http://twitter.com/%s/status/%s" ((tweet :tweeter) :_id) (tweet :_id))))

(defn respond [replyfn tweet code]
  (if-not (or (nil? code) (= code :is-me))
    (let [name (:name (:tweeter tweet))
          tweet-id (:_id tweet)
          text (m/get-message code)
          url (tweet-url tweet code)
          ]
      (replyfn tweet-id
               (format "@%s %s %s" name text url)))))
