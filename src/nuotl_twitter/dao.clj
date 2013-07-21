(ns nuotl-twitter.dao
  (:require [monger.collection :as mc]
            [monger.core :as mg]
            [monger.joda-time]
            [clojure.tools.logging :as log]
            [clj-http.client :as client]
            [cheshire.core :as json]
            ))

(defn connect-to-db [config]
  (log/debug (format "Connecting to database [%s]." (get-in config [:mongo :database])))
  (mg/connect! {:host (get-in config [:mongo :host])
                :port (get-in config [:mongo :port])})
  (mg/set-db! (mg/get-db (get-in config [:mongo :database]))))

(defn post-json [path data]
  (client/post (str "http://localhost:40000/" path)
               {:body (json/generate-string data)
                :content-type :json}))

(defn get-area-ids []
  (keys (json/parse-string (:body (client/get "http://localhost:40000/areas")))))

(defn add-event [event]
  (log/debug (format  "Persisting event: %s" event))
  (post-json "events" event)
  (log/debug (format "Persisted event.")))

(defn add-reply-id [reply-id event-tweet-id]
  (mc/save "reply" {:_id reply-id :event-id event-tweet-id}))

(defn get-reply-ids [event-tweet-id]
  (mc/find-maps "reply" {:event-id event-tweet-id}))

(defn remove-reply-id [reply-id]
  (mc/remove-by-id "reply" reply-id))

(defn remove-event [id]
  (mc/remove-by-id "event" id))

(defn get-tweeter [id]
  (mc/find-map-by-id "tweeter" id))

(defn add-or-update-tweeter [tweeter]
  (log/debug (format "Persisting tweeter: %s" tweeter))
  (mc/update "tweeter" (select-keys tweeter [:_id]) tweeter :upsert true))

(defn tweeter-approved? [id]
  (= (:approved (get-tweeter id)) "Y"))
