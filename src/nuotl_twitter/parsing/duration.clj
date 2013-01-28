(ns nuotl-twitter.parsing.duration
  (:require [clojure.string :as s])
  )

(defn ceil [number]
  (int (Math/ceil (double number))))

(defn parse-duration [string]
  (let [lastIndex (dec (count string))]
    (let [numb (subs string 0 lastIndex)
          mult (subs string lastIndex)]
      (if-let [multiplier
               (case (s/lower-case mult) "m" 1 "h" 60 "d" 1440 nil)]
        (let [number (read-string numb)]
          (if (and (number? number) (pos? number))
            (ceil  (* number multiplier))
            nil
            ))
        nil))))
