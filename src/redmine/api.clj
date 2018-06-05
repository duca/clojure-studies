;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/

(ns redmine.api
  (:gen-class)
  (:require
   [cheshire.core :refer :all]
   [clj-http.client :as client])
  )

(defn generate-base-url
  "Using the server url, returns a hash-map of common url string representations"
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
  (parse-string (:body (client/get url))))

(defn list-projects
  "List projects"
  [redmine-url]
  (let [proj_resp (http-resp-body (:list-projects (generate-base-url redmine-url)
                                                  )
                                  )
        ]
    proj_resp
    )
  )

(defn list-all-issues
  "List all issues"
  [redmine-url]
  (let [issues (http-resp-body (:all-issues (generate-base-url redmine-url)
                                            )
                               )
        ]
    issues
    )
  )

(defn project-exists?
  "check if a give project exists"
  [redmine-url project-id]
  (let [all_projects (http-resp-body (:list-projects (generate-base-url redmine-url)))
        names (for [project (get all_projects "projects")] (get project "name"))
        ]
    (> (.indexOf names project-id) 0)
    )
  )

(defn fetch-project-versions
  " Check if a given project id exists then returns a map with version {version_name version_json}"
  [redmine-url project-id]
  (if (not (project-exists? redmine-url project-id))
    (throw (AssertionError. (str "Project id " project-id " does not exist!"))))
  (let [raw_versions
        (http-resp-body (format (:project-versions (generate-base-url redmine-url)) project-id)
                        )]
    (reduce
     (fn [initial item]
       (into initial (hash-map (get item "name"), item))
       )
     {} (get raw_versions "versions")
     )
    )
  )

(defn fetch-project-issues
  "Returns a map containing all the issues organized by
  :name
  :status
  :version
  :start_date
  :end_date
  Paging example:
  GET /issues.xml?offset=0&limit=100
  GET /issues.xml?offset=100&limit=100

  preciso pensar no esquema de paginacao e incluir outras paradas na url (talvez so limitar essas funcoes a geracao de url)
  "
  [redmine-url project-id]
    (let [raw_issues
        (http-resp-body (format (:project-issues (generate-base-url redmine-url)) project-id)
                        )]
      (println (get raw_issues "issues"))
    ;; (reduce
    ;;  (fn [initial item]
    ;;    (into initial (hash-map (get item "name"), item))
    ;;    )
    ;;  {} (get raw_versions "versions")
    ;;  )
    )
  )

(defn version-properties-summary
  "Returns a map containing
  :project
  :version name
  :due date
  :status
  :updated_on
  "
  [redmine-url project-id version-id]
  (let [version-prop (get (fetch-project-versions redmine-url project-id) version-id)]
    (hash-map
     :name version-id,
     :project project-id,
     :due (get version-prop "due_date"),
     :status (get version-prop "status"),
     :updated_on 
     )
    )
  )

(prn (fetch-project-issues "http://localhost:10083" "t_project"))
