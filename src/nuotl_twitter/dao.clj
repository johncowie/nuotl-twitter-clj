(ns nuotl-twitter.dao
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.joda-time]
            ))

(mg/connect!)
(mg/set-db! (mg/get-db "nuotl"))

(defn get-areas []
  (mc/find-maps "area" {}))

(defn- split-event [event]
  {:event (assoc event :tweeter ((event :tweeter) :_id)) :tweeter (event :tweeter)}
  )

(defn add-event [event]
  (let [components (split-event event)]
    (mc/save "event" (components :event))
    (mc/save "tweeter" (components :tweeter))))
