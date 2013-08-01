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

(defn- swap-in-urls [tweet output]
  (let [urls (tweet :urls)]
    (loop [i 0 text (:text (:event output))]
      (if (< i (count urls))
        (recur (inc i) (fix-text text (urls i)))
        {:output (assoc-in output [:event :text] text) :success true}))))

(defn is-me? [tweet output]
  (if (= (:_id (:tweeter tweet)) (:application-id tweet))
    {:output output :success false}
    {:output output :success true}))

(defn get-tweeter [tweet output]
  {:output (assoc output :tweeter (:tweeter tweet)) :success true})

(defn is-approved? [tweet output]
  (if (contains?
       (set (get-in (conf/config) [:twitter :authorised]))
       (. (:name (:tweeter tweet)) toLowerCase))
    {:output output :success true}
    (if (dao/tweeter-approved? (:_id (:tweeter tweet)))
      {:output output :success true}
      {:output (assoc output :message :unapproved) :success false})))

(defn is-mention? [tweet output]
  (if (=
       (str "@" (. (:application-screen-name tweet) toLowerCase))
       (nth (clojure.string/split (:text tweet) #" ") 0))
    {:output output :success true}
    {:output output :success false}
    ))

(defn parse-tweet [tweet output]
  (let [parse-result (tweet-parser/parse-tweet (:text tweet))]
    (if (nil? (:event parse-result))
      {:output (assoc output :message (:error parse-result)) :success false}
      {:output (assoc output :event (merge (:event output) (:event parse-result))) :success true})))

(defn url-present? [tweet output]
  (if (empty? (:urls tweet))
    {:output (merge output {:event nil :message :no-url}) :success false}
    {:output output :success true}))

(defn clean-up-event [tweet output]
  (let [event  (merge
               (:event output)
               (select-keys tweet [:_id :tags :in-response-to]))]
    (let [event (assoc event :tweeter (:_id (:tweeter tweet)))]
      {:output (merge output {:event event :message :success}) :success true})))

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
  (loop [i 0 ret {:output {:event nil :tweeter nil :message nil} :success true}]
    (if (and (:success ret) (< i (count logic)))
      (recur (inc i) ((logic i) tweet (:output ret)))
      (:output ret))))
