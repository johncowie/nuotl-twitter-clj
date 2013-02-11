(ns nuotl-twitter.test-utils
  (:import ProcessingException))

(defn exception-with-code [code]
  (fn [e]
    (and (= ProcessingException (class e)) (= (. e (getErrorCode)) code))))
