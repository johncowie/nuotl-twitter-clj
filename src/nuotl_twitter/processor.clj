(ns nuotl-twitter.processor
  (:require [nuotl-twitter.parsing.tweet :as tweet-parser]
            [nuotl-twitter.dao :as dao]
            ))

(defn id-approved? [id]
  (if-let [tweeter (dao/get-tweeter id)]
    (= (tweeter :approved) "Y")
    false))

(defn html-url [display expanded]
  (format "<a href=\"%s\">%s</a>" expanded display))

(defn fix-text [text, url]
  (clojure.string/replace
   text
   (re-pattern (url :url))
   (html-url (url :display-url) (url :expanded-url))))

(defn swap-in-urls [tweet]
  (let [urls (tweet :urls)]
    (loop [i 0 text (tweet :text)]
      (if (< i (count urls))
        (recur (inc i) (fix-text text (urls i)))
        (assoc tweet :text text)))))

(defn process-tweet-text [tweet]
  (merge tweet (tweet-parser/parse-tweet (tweet :text))))

(defn process-tweet [tweet listener-id]
  (if-not (= ((tweet :tweeter) :_id) listener-id)
    (if (not (id-approved? ((tweet :tweeter) :_id)))
      (do (dao/add-tweeter (tweet :tweeter) false)
          :unapproved)
      (let [parsed-tweet (process-tweet-text tweet)]
        (if (contains? parsed-tweet :error)
          (parsed-tweet :error)
          (do
            (dao/add-tweeter (tweet :tweeter) true)
            (dao/add-event
             (assoc
                 (dissoc (swap-in-urls parsed-tweet) :urls)
               :tweeter
               ((tweet :tweeter) :_id)))
            :success
            ))))
     :from-listener-so-ignored
    ))
