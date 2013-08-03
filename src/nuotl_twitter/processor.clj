(ns nuotl-twitter.processor
  (:require [nuotl-twitter.parsing.tweet :as tweet-parser]
            [nuotl-twitter.dao :as dao]
            [nuotl-twitter.config :as conf]))

(defn- html-url [display expanded]
  (format "<a href=\"%s\">%s</a>" expanded display))

(defn- fix-text [text, url]
  (clojure.string/replace
   text
   (re-pattern (url :url))
   (html-url (url :display-url) (url :expanded-url))))

(defrecord Return [output success])

(defn- swap-in-urls [tweet output]
  (let [urls (tweet :urls)]
    (loop [i 0 text (:text (:event output))]
      (if (< i (count urls))
        (recur (inc i) (fix-text text (urls i)))
        (Return. (assoc-in output [:event :text] text) true)))))

(defn is-me? [tweet output]
  (if (= (:_id (:tweeter tweet)) (:application-id tweet))
    (Return. output false)
    (Return. output true)))

(defn get-tweeter [tweet output]
  (Return. (assoc output :tweeter (:tweeter tweet)) true))

(defn is-approved? [tweet output]
  (if (contains?
       (set (get-in (conf/config) [:twitter :authorised]))
       (. (:name (:tweeter tweet)) toLowerCase))
    {:output output :success true}
    (if (dao/tweeter-approved? (:_id (:tweeter tweet)))
      (Return. output true)
      (Return. (assoc output :message :unapproved) false))))

(defn is-mention? [tweet output]
  (if (=
       (str "@" (. (:application-screen-name tweet) toLowerCase))
       (nth (clojure.string/split (:text tweet) #" ") 0))
    (Return. output true)
    (Return. output false)
    ))

(defn parse-tweet [tweet output]
  (let [parse-result (tweet-parser/parse-tweet (:text tweet))]
    (if (nil? (:event parse-result))
      (Return.(assoc output :message (:error parse-result)) false)
      (Return.(assoc output :event (merge (:event output) (:event parse-result))) true))))

(defn url-present? [tweet output]
  (if (empty? (:urls tweet))
    (Return. (merge output {:event nil :message :no-url}) false)
    (Return. output true)))

(defn clean-up-event [tweet output]
  (let [event  (merge
               (:event output)
               (select-keys tweet [:_id :tags :in-response-to]))]
    (let [event (assoc event :tweeter (:_id (:tweeter tweet)))]
      (Return. (merge output {:event event :message :success}) true))))

(def logic [
            is-me?
            is-mention?
            get-tweeter
            is-approved?
            parse-tweet
            url-present?
            swap-in-urls
            clean-up-event
            ])

(defn process-tweet [tweet]
  (loop [i 0 ret (Return. {:event nil :tweeter nil :message nil} true)]
    (if (and (:success ret) (< i (count logic)))
      (recur (inc i) ((logic i) tweet (:output ret)))
      (:output ret))))
