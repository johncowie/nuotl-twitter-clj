(ns nuotl-twitter.parsing.tweet
  (:require [nuotl-twitter.parsing.date :as date]
            [nuotl-twitter.parsing.time :as time]
            [nuotl-twitter.parsing.duration :as duration]
            [nuotl-twitter.parsing.area :as area]
            ))

(def tweet-structure [{ :part 1 :function #(date/parse-date %) :error "Date problem"}
                      { :part 2 :function #(time/parse-time %) :error "Time problem"}
                      { :part 3 :function #(duration/parse-duration %) :error "Duration problem"}
                      { :part 4 :function #(area/parse-area %) :error "Area problem"}
                      ])

(defn get-parts [text]
   (clojure.string/split text #" " 6))

(defn parse-tweet [text]
  (let [parts (get-parts text)
        ret (transient {:value {} :message :none})]
    {:value nil :message :unimplemented}))
