(ns nuotl-twitter.core
  ;(:import [twitter4j TwitterStreamFactory TwitterFactory])
  (:require [cheshire.core :as j]
            [nuotl-twitter.processor :as p]
            [nuotl-twitter.responder :as r]
            [nuotl-twitter.dao :as dao]
            [compojure.core :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn configuration [file]
  (twitter4j.conf.PropertyConfiguration. (clojure.java.io/input-stream file)))

(defn- reply-to-tweet [twitter tweet-id message]
  (let [status (. (twitter4j.StatusUpdate. message) (inReplyToStatusId tweet-id))]
    (. twitter (updateStatus status))))

(defn- handle-delete [tweet-id user-id twitter twitter-id]
  (if (not= twitter-id user-id)
    (do
      (println (format "DELETING: %s" tweet-id))
      (dao/remove-event tweet-id)
      (doseq [r (dao/get-reply-ids tweet-id)]
        (. twitter (destroyStatus (r :_id)))
        (dao/remove-reply-id (r :_id))
        ))))

(defn- get-reply-fn [twitter]
  (fn [tweet-id message]
    (reply-to-tweet twitter tweet-id message)))

(defn- handle-tweet [tweet twitter twitter-id]
  (println tweet)
  (if (not= ((tweet :tweeter) :_id) twitter-id)
    (try
      (do
        (p/process-tweet tweet)
        (r/respond (get-reply-fn twitter) tweet :success))
      (catch Exception e
        (let [error-code (read-string (. e (getMessage)))]
          (r/respond (get-reply-fn twitter) tweet error-code)
          )))
     (dao/add-reply-id (tweet :_id) (tweet :in-response-to))
    ))

(defn listener [twitter twitter-id]
  (ClojureStatusListener.
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
      (let [stream (. (twitter4j.TwitterStreamFactory. config) (getInstance))
            twitter (. (twitter4j.TwitterFactory. config) (getInstance))
            twitter-id (. stream (getId))
            ]
        (. stream (addListener (listener twitter twitter-id)))
        (. stream (user))))))

(defn -main [& args]
  (.start (Thread. #(jetty/run-jetty app {:port 5000})))
  (start-twitter args)
  )
