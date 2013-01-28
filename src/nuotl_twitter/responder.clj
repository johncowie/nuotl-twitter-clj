(ns nuotl-twitter.responder)

(def messages {
               :success "Thanks! Your event tweet was successful."
               :unapproved "I'm afraid you are not yet authorized to use this service."
               :not-enough-words "Your event tweet doesn't have enough parts to be valid."
               :date "Sorry, I can't understand the date."
               :time "Sorry, I can't understand the time."
               :duration "Sorry, I can't understand the duration."
               :area "Sorry, I can't understand the area code."
               })

(defn- tweet-url [user-id tweet-id]
  (format "http://twitter.com/%s/status/%s" user-id tweet-id))

(defn respond [replyfn tweet code]
  (if-not (or (nil? code) (= code :from-listener-so-ignored))
    (let [name (:name (:tweeter tweet))
          tweet-id (:_id tweet)
          user-id (:_id (:tweeter tweet))
          text (messages code)]
      (replyfn tweet-id
               (format "@%s %s %s" name text (tweet-url user-id tweet-id))))))
