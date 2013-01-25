(ns nuotl-twitter.dao
  (:require [monger.collection :as mc]
            [monger.joda-time]
            ))

(defn add-feature []
  (mc/save "feature" )
  )

(defn get-areas []
  (mc/find-maps "area" {}))

(defn add-event [event]
  (mc/save "event" event))

(defn get-tweeter [id]
  (mc/find-map-by-id "tweeter" id))

(defn add-tweeter [tweeter approved?]
  (if approved?
    (mc/save "tweeter" (assoc tweeter :approved "Y"))
    (mc/save "tweeter" (assoc tweeter :approved "N"))))
