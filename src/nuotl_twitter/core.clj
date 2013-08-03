(ns nuotl-twitter.core
  (:require [cheshire.core :as j]
            [nuotl-twitter.processor :as p]
            [nuotl-twitter.responder :as r]
            [nuotl-twitter.dao :as dao]
            [nuotl-twitter.config :as config]
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
  (let  [reply (. twitter
                     (updateStatus
                      (.
                       (twitter4j.StatusUpdate. message)
                       (inReplyToStatusId tweet-id))))]
    (dao/add-reply-id (. reply (getId)) tweet-id)))

(defn- handle-delete [tweet-id user-id twitter]
  (if (not= (. twitter (getId)) user-id)
    (do
      (log/debug (format "DELETING: %s" tweet-id))
      (dao/remove-event tweet-id)
      (doseq [r (dao/get-reply-ids tweet-id)]
        (log/debug (str "Destroying reply with id [" (r :_id) "]"))
        (. twitter (destroyStatus (r :_id)))
        (dao/remove-reply-id (r :_id))
        ))))

(defn- get-reply-fn [twitter]
  (fn [tweet-id message]
    (reply-to-tweet twitter tweet-id message)))

(defn- handle-tweet [tweet twitter]
  (log/debug tweet)
  (let [processing-result (p/process-tweet tweet)]
    (let [event (:event processing-result)
          tweeter (:tweeter processing-result)
          message-code (:message processing-result)]
      (if-not (nil? event)
        (dao/add-event event)
        (log/debug "No event so not added"))
      (if-not (nil? tweeter)
        (dao/add-or-update-tweeter tweeter)
        (log/debug "No tweeter so not added"))
      (if-not (nil? message-code)
        (r/respond
         (get-reply-fn twitter) tweet message-code (:start event))
        (log/debug "No message code so no reply")
        ))))

(defn listener [twitter]
  (ClojureStatusListener.
   (. twitter (getId))
   (. twitter (getScreenName))
   #(handle-tweet % twitter)  ; status
   #(handle-delete %1 %2 twitter) ; deletion
   #(log/warn %) ; exception
   ))

(defroutes app-routes
  (GET "/" [] "PING"))

(def app
  (site app-routes))

(defn start-twitter [config]
  (dao/connect-to-db config)
  (let [twitter-config (configuration (get-in config [:twitter :properties-file]))]
    (let [stream (. (TwitterStreamFactory. twitter-config) (getInstance))
          twitter (. (TwitterFactory. twitter-config) (getInstance))]
      (. stream (addListener (listener twitter)))
      (. stream (user)))))

(defn -main [& args]
  (let [config (config/load-config (nth args 0))]
    (.start (Thread. #(jetty/run-jetty app {:port (get-in config [:http :port])})))
    (start-twitter config)))
