;   Copyright (c) Cognitect, Inc. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(require
 '[datomic.api :as d]
 '[datomic.samples.repl :as repl])

(def conn (repl/scratch-conn))

(d/transact
 conn
 [{:db/ident :red}
  {:db/ident :green}
  {:db/ident :blue}
  {:db/ident :yellow}])

(defn make-idents
  [x]
  (mapv #(hash-map :db/ident %) x))

(def sizes [:small :medium :large :xlarge])

(def types [:shirt :pants :dress :hat])
	;;; ⇒ #'mbrainz/types

(def colors [:red :green :blue :yellow])
	;;; ⇒ #'mbrainz/colors

(d/transact conn (make-idents sizes))
	;;; ⇒ <err>
	;;;   CompilerException java.lang.RuntimeException: Unable to resolve symbol: make-idents in this context, compiling:(/Users/lewang/src/day-of-datomic/tutorial/schema_queries.clj:1:18)

(d/transact conn (make-idents types))
	;;; ⇒ <err>
	;;;   CompilerException java.lang.RuntimeException: Unable to resolve symbol: make-idents in this context, compiling:(/Users/lewang/src/day-of-datomic/tutorial/schema_queries.clj:1:18)

(repl/transact-all conn (repl/resource "day-of-datomic/social-news.edn"))
(def db (d/db conn))

;; find the idents of all schema elements in the system
(sort (d/q '[:find [?ident ...]
             :where [_ :db/ident ?ident]]
           db))

;; find just the attributes
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]]
           db))

;; find just the data functions
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/function ?e]]
           db))

;; documentation of a schema element
(-> db (d/entity :db.unique/identity) :db/doc)

;; complete details of a schema element
(-> db (d/entity :user/email) d/touch)

;; find attributes with AVET index
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [?e :db/index true]
             [_ :db.install/attribute ?e]]
           db))

;; find attributes in the user namespace
(sort (d/q '[:find [?ident ...] 
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [(namespace ?ident) ?ns]
             [(= ?ns "user")]]
           db))

;; find all reference attributes
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [?e :db/valueType :db.type/ref]]
           db))

;; find all attributes that are cardinality-many
(sort (d/q '[:find [?ident ...]
             :where
             [?e :db/ident ?ident]
             [_ :db.install/attribute ?e]
             [?e :db/cardinality :db.cardinality/many]]
           db))



