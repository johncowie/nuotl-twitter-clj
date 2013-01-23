(ns nuotl-twitter.parsing.tweet
  (:require [nuotl-twitter.parsing.date :as date]
            [nuotl-twitter.parsing.time :as time]
            [nuotl-twitter.parsing.duration :as duration]
            [nuotl-twitter.parsing.area :as area]
            [clj-time.core :as clj-time]
            ))

(def tweet-structure [{ :part 1 :id :date     :function #(date/parse-date %)         }
                      { :part 2 :id :time     :function #(time/parse-time %)         }
                      { :part 3 :id :duration :function #(duration/parse-duration %) }
                      { :part 4 :id :area     :function #(area/parse-area %)         }
                      ])

(defn get-parts [text]
   (clojure.string/split text #" " 6))

(defn parse-tweet-text [text]
  (let [parts (get-parts text)]
    (if (>= (count parts) 6)
      (loop [i 0 ret {}]
        (if (< i (count tweet-structure))
          (let [parse-map (tweet-structure i)]
            (if-let [val ((parse-map :function) (parts (parse-map :part)))]
              (recur (inc i) (assoc ret (parse-map :id) val))
              {:error (parse-map :id)}))
          (assoc ret :text (parts 5))))
      {:error :not-enough-words})))

(defn merge-date-and-time [date time]
  (clj-time/date-time (clj-time/year date) (clj-time/month date) (clj-time/day date)
                      (clj-time/hour time) (clj-time/minute time) (clj-time/sec time)))

(defn infer-end-date [m]
  (let [start (merge-date-and-time (m :date) (m :time))]
    (let [end (clj-time/plus start (clj-time/minutes (m :duration)))]
      (dissoc (merge m {:start start :end end}) :date :time :duration)
      )))

(defn parse-tweet [text]
  (let [ret-map (parse-tweet-text text)]
    (if (nil? (ret-map :error))
      (infer-end-date ret-map)
      ret-map)))
