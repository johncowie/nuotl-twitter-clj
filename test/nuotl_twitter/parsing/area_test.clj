(ns nuotl-twitter.parsing.area-test
  (:use [midje.sweet])
  (:require [nuotl-twitter.parsing.area :as area]
            [nuotl-twitter.dao :as dao]
            [nuotl-twitter.test-utils :refer [exception-with-code]]
            ))

(facts
 (against-background
  (dao/get-area-ids) => '("n" "s")
  )
 (area/parse-area "cf") => (throws (exception-with-code :area-error))
 (area/parse-area "N") => :n
 (area/parse-area "n") => :n
 (area/parse-area "s") => :s
 )
