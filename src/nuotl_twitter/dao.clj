(ns nuotl-twitter.dao
  (:require [monger.collection :as mc]
            [monger.core :as mg]
            [monger.joda-time]
            [clojure.tools.logging :as log]
            ))

(defn connect-to-db [config]
  (log/debug (format "Connecting to database [%s]." (get-in config [:mongo :database])))
  (mg/connect! {:host (get-in config [:mongo :host]) :port (get-in config [:mongo :port])})
  (mg/set-db! (mg/get-db (get-in config [:mongo :database]))))

(defn get-area-ids []
  (map #(% :_id)
       (mc/find-maps "area")))

(defn add-event [event]
  (println event)
  (mc/save "event" event))

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
  (mc/update "tweeter" (select-keys tweeter [:_id]) tweeter :upsert true))

(defn tweeter-approved? [id]
  (= (:approved (get-tweeter id)) "Y"))
