(ns nuotl-twitter.parsing.tweet
  (:require [nuotl-twitter.parsing.date :as date]
            [nuotl-twitter.parsing.time :as time]
            [nuotl-twitter.parsing.duration :as duration]
            [nuotl-twitter.parsing.area :as area]
            [clj-time.core :as clj-time]
            ))

(def tweet-structure
  [
   {:part 1 :id :date     :function #(date/parse-date %)         :error-code :date-error}
   {:part 2 :id :time     :function #(time/parse-time %)         :error-code :time-error}
   {:part 3 :id :duration :function #(duration/parse-duration %) :error-code :duration-error}
   {:part 4 :id :area     :function #(area/parse-area %)         :error-code :area-error}
  ])

(defn- return-error [error-code]
  {:error error-code :event nil})

(defn- return-success [event]
  {:error nil :event event})


(defn- get-parts [text]
   (clojure.string/split text #" " 6))

(defn- parse-tweet-text [text]
  (let [parts (get-parts text)]
    (if (>= (count parts) 6) ;TODO shouldn't be hardcoded
      (loop [i 0 ret {}]
        (if (< i (count tweet-structure))
          (let [parse-map (tweet-structure i)]
            (if-let [val ((parse-map :function) (parts (parse-map :part)))]
              (recur (inc i) (assoc ret (parse-map :id) val))
              (return-error (parse-map :error-code))
              ))
          (return-success (assoc ret :text (parts 5)))))
      (return-error :too-short-error))))

(defn- merge-date-and-time [date time]
  (clj-time/date-time (clj-time/year date) (clj-time/month date) (clj-time/day date)
                      (clj-time/hour time) (clj-time/minute time) (clj-time/sec time)))

(defn- infer-end-date [m]
  (if (nil? (:error m))
    (let [event (:event m)]
      (let [start (merge-date-and-time (event :date) (event :time))]
        (let [end (clj-time/plus start (clj-time/minutes (event :duration)))]
          (return-success (dissoc (merge event {:start start :end end}) :date :time :duration))
          )))
    m
    ))

(defn- check-if-in-past [m]
  (if (nil? (:error m))
    (if (> (compare (clj-time/now) (:start (:event m))) 0)
      (return-error :in-past-error)
      m)
    m))

(defn parse-tweet [text]
  (check-if-in-past
   (infer-end-date
    (parse-tweet-text text))))
