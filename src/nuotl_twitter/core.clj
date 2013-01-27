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

(defn listener [id twitter]
  (ClojureStatusListener.
   #(do (println %) (r/respond
                     (fn [tw cd]  (reply-to-tweet twitter tw cd))
                     %
                     (p/process-tweet % id)))  ; status
   #(do (println (format "DELETING: %s" %)) (dao/remove-event %)) ; deletion
   #(println %) ; exception
   ))

(defn -main [& args]
  (let [props (nth args 0)
        db (if (> (count args) 1) (nth args 1) "nuotl")]
    (dao/connect-to-db db)
    (let [config (configuration props)]
      (let [stream (. (twitter4j.TwitterStreamFactory. config) (getInstance))
            twitter (. (twitter4j.TwitterFactory. config) (getInstance))]
        (. stream (addListener (listener (. stream (getId)) twitter)))
        (. stream (user))))))
