;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/

(ns redmine.url_defs
  (:gen-class)
  (:require
   [cheshire.core :as cheshire]
   [clj-http.client :as client]
   [clojure.core :refer :all]
   )
  )

(defn generate-base-url
  "Using the server url, returns a hash-map of common url string representations
  :redmine-url i.e. http://localhost:9000
  "
  [redmine-url]
  (let [redmine-project (str redmine-url "/projects/%s") ]
    (hash-map
     :list-projects (str redmine-url "/projects.json"),
     :all-issues (str redmine-url "/issues.json"),
     :project-props (str redmine-project ".json"),
     :project-versions (str redmine-project "/versions.json"),
     :project-issues (str redmine-project "/issues.json"),
     )
    )
  )

(defn http-resp-body
  "Uses clj-http to return the resp body"
  [url]
  (cheshire/parse-string (:body (client/get url))))

(defn list-projects-url
  "List projects"
  [redmine-url]
  (:list-projects (generate-base-url redmine-url))
  )

(defn project-exists-url
  "check if a give project exists"
  [redmine-url project-id]
  (:list-projects (generate-base-url redmine-url))
  )

(defn fetch-project-versions-url
  " Check if a given project id exists then returns a map with version {version_name version_json}"
  [redmine-url project-id]
  (format (:project-versions (generate-base-url redmine-url)) project-id)
  )


(defn update-map
  [m key val]
  (update m key (fn [foo] val))
  )

(prn (fetch-project-issues-url "http://localhost:10083" "t_project"))
(prn (client/get (fetch-project-issues-url "http://localhost:10083" "t_project")))
(def m redmine-api-modifiers)
