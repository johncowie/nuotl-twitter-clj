(ns nuotl-twitter.core
  ;(:import [twitter4j TwitterStreamFactory TwitterFactory])
  (:require [cheshire.core :as j]
            [nuotl-twitter.processor :as p]
            [nuotl-twitter.responder :as r]
            [nuotl-twitter.dao :as dao])
  (:gen-class))

(defn configuration [file]
  (twitter4j.conf.PropertyConfiguration. (clojure.java.io/input-stream file)))

(defn- reply-to-tweet [twitter tweet-id message]
  (let [status (. (twitter4j.StatusUpdate. message) (inReplyToStatusId tweet-id))]
    (. twitter (updateStatus status))))

(defn- get-reply-fn [twitter]
  (fn [tweet-id message]
    (reply-to-tweet twitter tweet-id message)))

(defn- handle-tweet [tweet twitter]
  (try
    (do
      (p/process-tweet tweet)
      (r/respond (get-reply-fn twitter) tweet :success))
    (catch Exception e
      (let [error-code (read-string (. e (getMessage)))]
        (r/respond (get-reply-fn twitter) tweet error-code)
        ))))

(defn listener [twitter]
  (ClojureStatusListener.
   #(do (println %) (handle-tweet %))  ; status
   #(do (println (format "DELETING: %s" %)) (dao/remove-event %)) ; deletion
   #(println %) ; exception
   ))

(defn -main [& args]
  (let [props (nth args 0)
        db (if (> (count args) 1) (nth args 1) "nuotl")]
    (dao/connect-to-db db)
    (let [config (configuration props)]
      (let [stream (. (twitter4j.TwitterStreamFactory. config) (getInstance))
            twitter (. (twitter4j.TwitterFactory. config) (getInstance))
            twitter-id (. stream (getId))
            ]
        (p/set-listener-id! twitter-id)
        (. stream (addListener (listener twitter)))
        (. stream (user))))))
