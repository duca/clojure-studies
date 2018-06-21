;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/

(ns redmine.url_defs
  (:gen-class)
  (:require
   [cheshire.core :as cheshire]
   [clj-http.client :as client]
   [clojure.core :refer :all]
   [clojure.core.async :as async]
   )
  )

(defn create_ticket
  [redmine-url name project]
  (let [proj_id (redmine.api/get-project-id redmine-url project)
        ticket
        {
         ;; :form-params
         ;; {
         ;;  :key "cbea309d1acc307bb3220e2e4f0c6549094a20f7"
         ;;  :project_id 1
         ;;  :status "asd"
         ;;  :tracker "aaa"
         ;;  :subject "Teste doido"
         ;;  :description "fazer o que"
         ;;  }
         :body (cheshire/generate-string
                {
                 :key "a437672889c9fa13a545d890f025f37153547067"
                 :issue {
                         :project_id proj_id
                         :status "asd"
                         :tracker "aaa"
                         :subject name
                         :description "fazer o que"
                         }
                 })
         :headers {"X-Api-Version" "2"}
         :content-type :json
         :socket-timeout 1000  ;; in milliseconds
         :conn-timeout 1000    ;; in milliseconds
         ;; :accept :json
         }]
    ;; (prn ticket)
    ;; (prn proj_id project)
    ;; (prn (cheshire/generate-string ticket))
    (when-not (= proj_id "")
      (client/post "http://localhost:10083/issues.json" ticket)
      )
    )
  )

(defn create_user
  [name]
  (let [ticket
        {
         ;; :form-params
         ;; {
         ;;  :key "cbea309d1acc307bb3220e2e4f0c6549094a20f7"
         ;;  :project_id 1
         ;;  :status "asd"
         ;;  :tracker "aaa"
         ;;  :subject "Teste doido"
         ;;  :description "fazer o que"
         ;;  }
         :body (cheshire/generate-string
                {
                 :key "cbea309d1acc307bb3220e2e4f0c6549094a20f7"
                 :user {
                        :login "remotamente"
                        :password "123456**01"
                        :firstname "Remoto"
                        :lastname "Controle"
                        :mail "fazeroque@blog.com"
                        }
                 })
         :headers {"X-Api-Version" "2"}
         :content-type :json
         :socket-timeout 1000  ;; in milliseconds
         :conn-timeout 1000    ;; in milliseconds
         ;; :accept :json
         }]
    (prn (cheshire/generate-string ticket))
    (client/post "http://localhost:10083/users.json" ticket)

    )
  )

(defn create_tickets
  [end]
  (doseq [x (range end)]
    (async/go
      (create_ticket "http://127.0.0.1:10083"
                     (format "mais_tickets_%d" x)
                     "t2_project")
      )
    )
  (prn "done")
  )


;;(create_user "remotamente")c
;;(create_ticket "http://127.0.0.1:10083" "teste1" "t_project")
