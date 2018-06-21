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

(defn list-all-issues-url
  "List all issues"
  [redmine-url]
  (:all-issues (generate-base-url redmine-url))
  )

(def redmine-api-modifiers
  {
   :set nil,
   :id nil,
   :project_id nil,
   :assigned_to_id nil,
   :status_id nil,
   :cf_1 nil,
   :sort nil,
   :offset nil,
   :limit nil,
   :created_on nil,
   :updated_on nil
   }
  )

(defn fetch-project-issues-url
  "Returns a map containing all the issues organized by
  :name
  :status
  :version
  :start_date
  :end_date
  Paging example:
  GET /issues.xml?offset=0&limit=100
  GET /issues.xml?offset=100&limit=100
  "

  (
   [redmine-url project-id modifiers]
   (let [initial_url
         (format (:project-issues (generate-base-url redmine-url)) project-id)]
     (def url (str initial_url "?"))
     (prn modifiers)
     (prn (-> url
         ;; limit
         (if (:set modifiers) (str url "set=" (:set modifiers) "&"))
         (if (:id modifiers) (str url "id=" (:id modifiers) "&"))
         (if (:project_id modifiers) (str url "project_id=" (:project_id modifiers) "&"))
         (if (:assigned_to_id modifiers) (str url "assigned_to_id=" (:assigned_to_id modifiers) "&"))
         (if (:status_id modifiers) (str url "status_id=" (:status_id modifiers) "&"))
         (if (:cf_1 modifiers) (str url "cf_1=" (:cf_1 modifiers) "&"))
         (if (:sort modifiers) (str url "sort=" (:sort modifiers) "&"))
         (if (:offset modifiers) (str url "offset=" (:offset modifiers) "&"))
         (if (:updated_on modifiers) (str url "updated_on=" (:updated_on modifiers) "&"))
         (if (:created_on modifiers) (str url "created_on=" (:created_on modifiers) "&"))
         (if (:limit modifiers) (str url  "limit=" (:limit modifiers) "&"))
         (if (:limit modifiers) (prn url))
         ))
     )
   )
  ;; simple arity
  (
   [redmine-url project-id]
   (str (format (:project-issues (generate-base-url redmine-url)) project-id)
        "?offset=0&limit=10000")

   )
  )
