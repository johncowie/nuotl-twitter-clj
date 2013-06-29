(defproject nuotl-twitter "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.twitter4j/twitter4j-core "3.0.3"]
                 [org.twitter4j/twitter4j-async "3.0.3"]
                 [org.twitter4j/twitter4j-stream "3.0.3"]
                 [com.rubiconproject.oss/jchronic "0.2.6"]
                 [midje "1.4.0"]
                 [clj-time "0.4.4"]
                 [cheshire "5.0.0"]
                 [com.novemberain/monger "1.4.2"]
                 [log4j/log4j "1.2.17"]
                 [org.clojure/tools.logging "0.2.6"]
                 [compojure "1.1.3"]
                 [ring/ring-jetty-adapter "1.1.4"]
                 ]
  :main nuotl-twitter.core
  :java-source-paths ["src/java" "test/java"]
  :plugins [[lein-midje "2.0.1"]]
  )
