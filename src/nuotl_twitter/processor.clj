(ns nuotl-twitter.processor
  (:require [nuotl-twitter.parsing.tweet :as tweet-parser]
            [nuotl-twitter.dao :as dao]
            ))

(defn- id-approved? [id]
  (if-let [tweeter (dao/get-tweeter id)]
    (= (tweeter :approved) "Y")
    false))

(defn- html-url [display expanded]
  (format "<a href=\"%s\">%s</a>" expanded display))

(defn- fix-text [text, url]
  (clojure.string/replace
   text
   (re-pattern (url :url))
   (html-url (url :display-url) (url :expanded-url))))

(defn- swap-in-urls [tweet]
  (let [urls (tweet :urls)]
    (loop [i 0 text (tweet :text)]
      (if (< i (count urls))
        (recur (inc i) (fix-text text (urls i)))
        (assoc tweet :text text)))))

(defn- clean-up-parsed-tweet [tweet parsed-tweet]
  (assoc
      (dissoc (swap-in-urls parsed-tweet) :tweeter :urls :in-response-to)
      :tweeter ((tweet :tweeter) :_id)))

(defn- process-tweet-text [m]
  (let [tweet (m :tweet)
        parsed-tweet (clean-up-parsed-tweet
                      tweet (merge tweet (tweet-parser/parse-tweet (tweet :text))))]
    (dao/add-tweeter (tweet :tweeter) true)
    (assoc m :processed parsed-tweet)))

(defn- check-approval [m]
  (let [tweet (m :tweet)]
    (if-not (id-approved? ((tweet :tweeter) :_id))
      (do
        (dao/add-tweeter (tweet :tweeter) false)
        (throw (ProcessingException. :unapproved)))))
  m)

(defn- check-for-url [m]
  (let [tweet (m :tweet)]
    (if (empty? (tweet :urls))
      (throw (ProcessingException. :no-url))))
  m)

(defn process-tweet [tweet]
  (let [processed
        (:processed
         (-> {:tweet tweet}
             check-approval
             process-tweet-text
             check-for-url))]
    (dao/add-event processed)
    processed))
