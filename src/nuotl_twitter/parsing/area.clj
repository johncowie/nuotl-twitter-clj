(ns nuotl-twitter.parsing.area
  (:require [nuotl-twitter.dao :as dao])
  )

(defn parse-area [string]
  (let [id (clojure.string/lower-case string)]
    (if (some #{id} (dao/get-area-ids)) (keyword id) nil)))
