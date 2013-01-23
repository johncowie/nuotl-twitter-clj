(ns nuotl-twitter.dao
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.joda-time]
            ))

(mg/connect!)
(mg/set-db! (mg/get-db "nuotl"))

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
