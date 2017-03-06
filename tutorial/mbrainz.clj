(ns mbrainz
  (:require [datomic.api :as d]))

(require '[datomic.api :as d])

(def uri "datomic:dev://localhost:4334/mbrainz-1968-1973")

(def conn (d/connect uri))

(def db (d/db conn))

(d/q '[:find ?id ?type ?gender
       :in $ ?name
       :where
       [?e :artist/name ?name]
       [?e :artist/gid ?id]
       [?e :artist/type ?teid]
       [?teid :db/ident ?type]
       [?e :artist/gender ?geid]
       [?geid :db/ident ?gender]]
     db
     "Janis Joplin")

(ns mbrainz
  (:require [datomic.api :as d]))

(defn match? [m] (re-find (re-pattern "so (\\d+)") m))


(->> (d/q '[:find ?e ?m
            :in $
            :where
            [?e :user/regex-match ?r]
            [(mbrainz/match? ?r) ?m]]
          [[1 :user/regex-match "so 111"]
           [2 :user/regex-match "so 222"]
           [3 :user/regex-match "blah 333"]]))

(d/q '[:find (count ?eid) .
       :where [?eid :artist/name]
       [?eid :artist/country :country/CA]]
     db)
	;;; ⇒ 63
