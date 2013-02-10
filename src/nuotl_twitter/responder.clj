(ns nuotl-twitter.responder
  (:require [nuotl-twitter.messages :as m]
            [clj-time.core :as t]))

(defn- tweet-url [tweet code]
  (if (= code :success)
    (format "http://nextupontheleft.org/events#%s" (tweet :_id))
    (format "http://twitter.com/%s/status/%s" ((tweet :tweeter) :_id) (tweet :_id))))

(defn respond [replyfn tweet code]
  (println "CODE: " code)
  (if-not (or (nil? code) (= code :is-me))
    (let [name (:name (:tweeter tweet))
          tweet-id (:_id tweet)
          text (m/get-message code)
          url (tweet-url tweet code)
          ]
      (replyfn tweet-id
               (format "@%s %s %s" name text url)))))
