(defproject clj-redmine "0.1.0-SNAPSHOT"
  :description "A small project to implement the redmine api (redmine version 3.4.4)"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.

  :dependencies [
                 [org.clojure/clojure "1.9.0"]
                 [clj-http "3.9.0"]
                 [cheshire "5.8.0"]
                 [org.clojure/core.async "0.4.474"]
                 ]
  :main ^:skip-aot redmine.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
