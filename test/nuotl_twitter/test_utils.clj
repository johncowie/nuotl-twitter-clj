(ns nuotl-twitter.test-utils
  (:import ProcessingException))

(defn exception-with-code [code]
  (fn [e]
    (= (. e (getErrorCode)) code)))
