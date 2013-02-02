(ns nuotl-twitter.messages)

(def messages {
               :time-error "Invalid time."
               :date-error "Invalid date."
               :area-error "Invalid area."
               :duration-error "Invalid duration."
               :unapproved "You are unauthorized to use this service."
               :too-short-error "Tweet is too short to have all the required parts."
               :success "Your event tweet was successful!"
               })

(defn get-message [s]
  (messages s))
