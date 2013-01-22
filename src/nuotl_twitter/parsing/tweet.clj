(ns nuotl-twitter.parsing.tweet
  (:require [nuotl-twitter.parsing.date :as date]
            [nuotl-twitter.parsing.time :as time]
            [nuotl-twitter.parsing.duration :as duration]
            [nuotl-twitter.parsing.area :as area]
            ))

(def tweet-structure [{ :part 1 :id :date     :function #(date/parse-date %)         :error "Date problem"}
                      { :part 2 :id :time     :function #(time/parse-time %)         :error "Time problem"}
                      { :part 3 :id :duration :function #(duration/parse-duration %) :error "Duration problem"}
                      { :part 4 :id :area     :function #(area/parse-area %)         :error "Area problem"}
                      ])

(defn get-parts [text]
   (clojure.string/split text #" " 6))

(defn parse-tweet [text]
  (let [parts (get-parts text)
        ret (transient {:value {} :message :none})]
    (if (>= (count parts) 6)
      (loop [i 0 ret {:value {} :message :none}]
        (if (< i (count tweet-structure))
          (let [parse-map (tweet-structure i)]
            (if-let [val ((parse-map :function) (parts (parse-map :part)))]
              (recur (inc i) (assoc-in ret [:value (parse-map :id)] val))
              {:value nil :message (parse-map :error)}))
          (assoc ret :message "Success")))
      {:value nil :message "Not enought parts"})))
