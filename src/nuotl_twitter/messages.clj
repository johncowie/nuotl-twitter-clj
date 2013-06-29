(ns nuotl-twitter.messages)

(def messages {
               :time-error "Invalid time."
               :date-error "Invalid date."
               :area-error "Invalid area."
               :duration-error "Invalid duration."
               :unapproved "You are unauthorized to use this service."
               :too-short-error "Tweet is too short to have all the required parts."
               :in-past-error "Your event cannot start in the past."
               :no-url "Your event tweet should have an error."
               :success "Your event tweet was successful!"
               })

(defn get-message [s]
  (if-let [message (messages s)]
    message
    (throw (Exception. (format  "No message found for code %s" s)))
    ))
