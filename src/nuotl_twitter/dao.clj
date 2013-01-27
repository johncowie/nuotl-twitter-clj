(ns nuotl-twitter.dao
  (:require [monger.collection :as mc]
            [monger.core :as mg]
            [monger.joda-time]
            ))

(defn connect-to-db [db]
  (mg/connect!)
  (mg/set-db! (mg/get-db db)))

(defn get-area-ids []
  (map #(% :_id)
       (mc/find-maps "area")))

(defn add-event [event]
  (mc/save "event" event))

(defn remove-event [id]
  (mc/remove-by-id "event" id))

(defn get-tweeter [id]
  (mc/find-map-by-id "tweeter" id))

(defn add-tweeter [tweeter approved?]
  (if approved?
    (mc/save "tweeter" (assoc tweeter :approved "Y"))
    (mc/save "tweeter" (assoc tweeter :approved "N"))))
