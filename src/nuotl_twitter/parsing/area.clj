(ns nuotl-twitter.parsing.area)

(def areas
  {
   :n {:name "North London" :region :london}
   :cf {:name "Cardiff" :region :wales}
   }
  )

(defn parse-area [string]
  (let [id (keyword (clojure.string/lower-case string))]
    (if (contains? areas id) id nil)))
