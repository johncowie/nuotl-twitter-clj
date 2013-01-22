(ns nuotl-twitter.parsing.area)

(def areas
  {
   :n {:name "North London" :region :london}
  }
  )

(defn parse-area [string]
  (areas (keyword (clojure.string/lower-case string)))
  )
