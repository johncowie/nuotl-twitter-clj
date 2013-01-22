(ns nuotl-twitter.core
  (:require [cheshire.core :as j])
  (:gen-class))

(defn configuration [file]
  (twitter4j.conf.PropertyConfiguration. (clojure.java.io/input-stream file)))

(defn json-parser [json] (println json) (println (:id json)))
(def listener (ClojureStatusListener. json-parser #(println %) #(println %)))

(defn -main [& args]
  (let [stream
        (. (twitter4j.TwitterStreamFactory. (configuration (nth args 0))) (getInstance))]
  (. stream (addListener listener))
  (. stream (user))))
