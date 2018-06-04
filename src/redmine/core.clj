(ns redmine.core
  (:gen-class)
  (:require
   [redmine.api :refer :all]
   [cheshire.core :refer :all]
   [clj-http.client :as client]
   ))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def red-url "http://localhost:10083")
  (list-projects red-url)
  ;; (for [i res]
  ;;   (println (get i "name"))
  ;;   )

  )



(-main)
