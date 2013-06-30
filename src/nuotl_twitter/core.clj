(ns nuotl-twitter.core
  (:require [cheshire.core :as j]
            [nuotl-twitter.processor :as p]
            [nuotl-twitter.responder :as r]
            [nuotl-twitter.dao :as dao]
            [compojure.core :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [ring.adapter.jetty :as jetty]
            [clojure.tools.logging :as log])
  (:import [twitter4j StatusUpdate Twitter TwitterFactory TwitterStreamFactory]
           [twitter4j.conf PropertyConfiguration]
           [org.nextupontheleft.twitter ClojureStatusListener])
  (:gen-class))

(defn configuration [file]
  (PropertyConfiguration. (clojure.java.io/input-stream file)))

(defn- reply-to-tweet
  [twitter tweet-id message]
  (. twitter
     (updateStatus
      (.
       (twitter4j.StatusUpdate. message)
       (inReplyToStatusId tweet-id)))))

(defn- handle-delete [tweet-id user-id twitter twitter-id]
  (if (not= twitter-id user-id)
    (do
      (log/debug (format "DELETING: %s" tweet-id))
      (dao/remove-event tweet-id)
      (doseq [r (dao/get-reply-ids tweet-id)]
        (. twitter (destroyStatus (r :_id)))
        (dao/remove-reply-id (r :_id))
        ))))

(defn- get-reply-fn [twitter]
  (fn [tweet-id message]
    (reply-to-tweet twitter tweet-id message)))

(defn- handle-tweet [tweet twitter twitter-id]
  (log/debug tweet)
  (let [processing-result (p/process-tweet tweet)]
    (let [event (:event processing-result)
          tweeter (:tweeter processing-result)
          message-code (:message processing-result)]
      (if-not (nil? event)
        (dao/add-event event)
        (println "No event so not added"))
      (if-not (nil? tweeter)
        (dao/add-or-update-tweeter tweeter)
        (println "No tweeter so not added"))
      (if-not (nil? message-code)
        (r/respond
         (get-reply-fn twitter) tweet message-code (:start event))
        (println "No message code so no reply")
        ))))

(defn listener [twitter twitter-id]
  (ClojureStatusListener.
   twitter-id
   #(handle-tweet % twitter twitter-id)  ; status
   #(handle-delete %1 %2 twitter twitter-id) ; deletion
   #(println %) ; exception
   ))

(defroutes app-routes
  (GET "/" [] "PING"))

(def app
  (site app-routes))

(defn start-twitter [args]
  (let [props (nth args 0)
        db (if (> (count args) 1) (nth args 1) "nuotl")]
    (dao/connect-to-db db)
    (let [config (configuration props)]
      (let [stream (. (TwitterStreamFactory. config) (getInstance))
            twitter (. (TwitterFactory. config) (getInstance))
            twitter-id (. stream (getId))]
        (. stream (addListener (listener twitter twitter-id)))
        (. stream (user))))))

(defn -main [& args]
  (.start (Thread. #(jetty/run-jetty app {:port 5000})))
  (start-twitter args))
