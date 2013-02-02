(ns nuotl-twitter.responder
  (:require [nuotl-twitter.messages :as m]))

(defn- tweet-url [user-id tweet-id]
  (format "http://twitter.com/%s/status/%s" user-id tweet-id))

(defn respond [replyfn tweet code]
  (if-not (or (nil? code) (= code :from-listener-so-ignored))
    (let [name (:name (:tweeter tweet))
          tweet-id (:_id tweet)
          user-id (:_id (:tweeter tweet))
          text (m/get-message code)]
      (replyfn tweet-id
               (format "@%s %s %s" name text (tweet-url user-id tweet-id))))))
