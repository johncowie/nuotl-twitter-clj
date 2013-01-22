(ns nuotl-twitter.core
  (:require [cheshire.core :as j])
  (:gen-class))

(def credentials (clojure.java.io/input-stream (clojure.java.io/resource "twitter4j.properties")))

(def configuration (twitter4j.conf.PropertyConfiguration. credentials))

(defn json-parser [json] (println json) (println (:id json)))

(def listener (ClojureStatusListener. json-parser #(println %) #(println %)))

(defn -main [& args]
  (let [stream
        (. (twitter4j.TwitterStreamFactory. configuration) (getInstance))]
  (. stream (addListener listener))
  (. stream (user))))
