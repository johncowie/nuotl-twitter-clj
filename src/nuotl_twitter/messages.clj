(ns nuotl-twitter.messages
  (:require [clojure.tools.logging :as log]))

(def messages {
               :time-error "Invalid time."
               :date-error "Invalid date."
               :area-error "Invalid area."
               :duration-error "Invalid duration."
               :unapproved "You are unauthorized to use this service."
               :too-short-error "Tweet is too short to have all the required parts."
               :in-past-error "Your event cannot start in the past."
               :no-url "Your event tweet should have a url."
               :success "Your event tweet was successful!"
               })

(defn get-message [s]
  (if-let [message (messages s)]
    message
    (log/error (format  "No message found for code %s" s))))
