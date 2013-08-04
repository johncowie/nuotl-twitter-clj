(ns nuotl-twitter.dao
  (:require [monger.joda-time]
            [clojure.tools.logging :as log]
            [clj-http.client :as client]
            [cheshire.core :as json]
            ))

(defn- post-json [path data]
  (client/post (str "http://localhost:40000/" path)
               {:body (json/generate-string data)
                :content-type :json}))

(defn- get-json [path]
  (json/parse-string (:body (client/get (str "http://localhost:40000/" path))) #(keyword %)))

(defn- delete-resource [path]
  (client/delete (str "http://localhost:40000/" path)))

(defn get-area-ids []
  (map #(name %) (keys (get-json "areas"))))

(defn add-event [event]
  (log/debug (format  "Persisting event: %s" event))
  (post-json "events" event)
  (log/debug (format "Persisted event.")))

(defn add-reply-id [reply-id event-tweet-id]
  (post-json "replies" {:_id event-tweet-id :reply reply-id}))

(defn get-reply-id [id]
  (get-json (str "replies/" id)))

(defn remove-reply-id [event-tweet-id]
  (delete-resource (str "replies/" event-tweet-id)))

(defn remove-event [id]
  (delete-resource (str "events/" id)))

(defn get-tweeter [id]
  (get-json (str "tweeter/" id)))

(defn add-or-update-tweeter [tweeter]
  (log/debug (format "Persisting tweeter: %s" tweeter))
  (post-json "tweeters" tweeter))

(defn tweeter-approved? [id]
  (= (:approved (get-tweeter id)) "Y"))
