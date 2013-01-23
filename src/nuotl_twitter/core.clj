(ns nuotl-twitter.core
  (:require [cheshire.core :as j]
            [nuotl-twitter.processor :as p]
            [monger.core :as mg]
            )
  (:gen-class))

(defn configuration [file]
  (twitter4j.conf.PropertyConfiguration. (clojure.java.io/input-stream file)))

(defn listener [id] (ClojureStatusListener.
                     #(println (p/process-tweet % id)) #(println %) #(println %)))



(defn -main [& args]
  (let [props (nth args 0)
        db (if (> (count args) 1) (nth args 1) "nuotl")]
    (mg/connect!)
    (mg/set-db! (mg/get-db db))
    (let [stream
          (. (twitter4j.TwitterStreamFactory. (configuration props)) (getInstance))]
      (. stream (addListener (listener (. stream (getId)))))
      (. stream (user)))))
