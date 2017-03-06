(ns many-to-many)

;; https://gist.github.com/stuarthalloway/2158138


;; Datomic example code

;; make in memory database
(use '[datomic.api :only (q db) :as d])

(def uri "datomic:mem://matches")

(d/create-database uri)

(def conn (d/connect uri))

;; add the match attribute
(d/transact
 conn
 [{:db.install/_attribute :db.part/db,
   :db/id #db/id[:db.part/db],
   :db/ident :read/match,
   :db/valueType :db.type/ref,
   :db/cardinality :db.cardinality/many,
   :db/doc "URI of a database"}])

;; add some fake reads and matches, naming them with db/doc
(let [[read-1 read-2 match-a match-b match-c match-d]
      (repeatedly #(d/tempid :db.part/user))]
  (d/transact conn
              [{:db/id read-1, :db/doc "read-1" :read/match [match-a match-b match-c]}
               {:db/id read-2, :db/doc "read-2" :read/match [match-b match-c match-d]}
               {:db/id match-a, :db/doc "match-a"}
               {:db/id match-b, :db/doc "match-b"}
               {:db/id match-c, :db/doc "match-c"}
               {:db/id match-d, :db/doc "match-d"}]))

;; find all matches corresponding to read-1
(q '[:find ?match ?doc
     :where
     [?read :db/doc "read-1"]
     [?read :read/match ?match]
     [?match :db/doc ?doc]]
   (db conn))

;; find all reads corresponding to match-b
(q '[:find ?read ?doc
     :where
     [?read :db/doc ?doc]
     [?read :read/match ?match]
     [?match :db/doc "match-b"]]
   (db conn))
