;   Copyright (c) Cognitect, Inc. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

;; The examples below parallel http://docs.datomic.com/pull.html
;; sample data at https://github.com/Datomic/mbrainz-sample

;; get connected
(require '[datomic.api :as d])
(def uri "datomic:free://localhost:4334/mbrainz-1968-1973")
(def conn (d/connect uri))
(def db (d/db conn))
(set! *print-length* 100)
	;;; ⇒ 100

;; entities used in pull examples
(def led-zeppelin [:artist/gid #uuid "678d88b2-87b0-403b-b63d-5da7465aecc3"])
	;;; ⇒ #'mbrainz/led-zeppelin

(def mccartney [:artist/gid #uuid "ba550d0e-adac-4864-b88b-407cab5e76af"])
	;;; ⇒ #'mbrainz/mccartney

(def dark-side-of-the-moon [:release/gid #uuid "24824319-9bb8-3d1e-a2c5-b8b864dafd1b"])
	;;; ⇒ #'mbrainz/dark-side-of-the-moon

(def dylan-harrison-sessions [:release/gid #uuid "67bbc160-ac45-4caf-baae-a7e9f5180429"])
	;;; ⇒ #'mbrainz/dylan-harrison-sessions

(def dylan-harrison-cd (d/q '[:find ?medium .
                              :in $ ?release
                              :where
                              [?release :release/media ?medium]]
                            db
                            (java.util.ArrayList. dylan-harrison-sessions)))
	;;; ⇒ #'mbrainz/dylan-harrison-cd

(def ghost-riders (d/q '[:find ?track .
                         :in $ ?release ?trackno
                         :where
                         [?release :release/media ?medium]
                         [?medium :medium/tracks ?track]
                         [?track :track/position ?trackno]]
                       db
                       dylan-harrison-sessions
                       11))
	;;; ⇒ #'mbrainz/ghost-riders

(def concert-for-bangla-desh [:release/gid #uuid "f3bdff34-9a85-4adc-a014-922eef9cdaa5"])
	;;; ⇒ #'mbrainz/concert-for-bangla-desh

;; attribute name
(d/pull db [:artist/name :artist/startYear] led-zeppelin)
	;;; ⇒ {:artist/name "Led Zeppelin", :artist/startYear 1968}

(d/pull db [:artist/country] led-zeppelin)
	;;; ⇒ {:artist/country {:db/id 17592186045420}}

;; reverse lookup
(d/pull db [:artist/_country] :country/GB)
	;;; ⇒ {:artist/_country [{:db/id 17592186045758} {:db/id 17592186045759} {:db/id 17592186045765} {:db/id 17592186045766} {:db/id 17592186045779} {:db/id 17592186045809} {:db/id 17592186045829} {:db/id 17592186045837} {:db/id 17592186045842} {:db/id 17592186045855} {:db/id 17592186045859} {:db/id 17592186045864} {:db/id 17592186045867} {:db/id 17592186045870} {:db/id 17592186045880} {:db/id 17592186045881} {:db/id 17592186045888} {:db/id 17592186045922} {:db/id 17592186045925} {:db/id 17592186045942} {:db/id 17592186045965} {:db/id 17592186045973} {:db/id 17592186045994} {:db/id 17592186045997} {:db/id 17592186046001} {:db/id 17592186046002} {:db/id 17592186046005} {:db/id 17592186046015} {:db/id 17592186046018} {:db/id 17592186046045} {:db/id 17592186046047} {:db/id 17592186046053} {:db/id 17592186046076} {:db/id 17592186046081} {:db/id 17592186046092} {:db/id 17592186046132} {:db/id 17592186046134} {:db/id 17592186046135} {:db/id 17592186046149} {:db/id 17592186046160} {:db/id 17592186046169} {:db/id 17592186046171} {:db/id 17592186046184} {:db/id 17592186046188} {:db/id 17592186046193} {:db/id 17592186046199} {:db/id 17592186046207} {:db/id 17592186046210} {:db/id 17592186046218} {:db/id 17592186046220} {:db/id 17592186046225} {:db/id 17592186046228} {:db/id 17592186046247} {:db/id 17592186046252} {:db/id 17592186046276} {:db/id 17592186046288} {:db/id 17592186046289} {:db/id 17592186046298} {:db/id 17592186046310} {:db/id 17592186046311} {:db/id 17592186046313} {:db/id 17592186046328} {:db/id 17592186046332} {:db/id 17592186046348} {:db/id 17592186046357} {:db/id 17592186046379} {:db/id 17592186046384} {:db/id 17592186046385} {:db/id 17592186046390} {:db/id 17592186046411} {:db/id 17592186046417} {:db/id 17592186046421} {:db/id 17592186046437} {:db/id 17592186046438} {:db/id 17592186046439} {:db/id 17592186046444} {:db/id 17592186046445} {:db/id 17592186046467} {:db/id 17592186046473} {:db/id 17592186046486} {:db/id 17592186046494} {:db/id 17592186046505} {:db/id 17592186046512} {:db/id 17592186046520} {:db/id 17592186046557} {:db/id 17592186046560} {:db/id 17592186046561} {:db/id 17592186046596} {:db/id 17592186046610} {:db/id 17592186046626} {:db/id 17592186046643} {:db/id 17592186046650} {:db/id 17592186046659} {:db/id 17592186046662} {:db/id 17592186046667} {:db/id 17592186046674} {:db/id 17592186046693} {:db/id 17592186046694} {:db/id 17592186046695} {:db/id 17592186046711} ...]}

;; component defaults
(d/pull db [:release/media] dark-side-of-the-moon)
	;;; ⇒ {:release/media [{:db/id 17592186121277, :medium/format {:db/id 17592186045741}, :medium/position 1, :medium/trackCount 10, :medium/tracks [{:db/id 17592186121278, :track/duration 68346, :track/name "Speak to Me", :track/position 1, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121279, :track/duration 168720, :track/name "Breathe", :track/position 2, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121280, :track/duration 230600, :track/name "On the Run", :track/position 3, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121281, :track/duration 409600, :track/name "Time", :track/position 4, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121282, :track/duration 284133, :track/name "The Great Gig in the Sky", :track/position 5, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121283, :track/duration 382746, :track/name "Money", :track/position 6, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121284, :track/duration 469853, :track/name "Us and Them", :track/position 7, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121285, :track/duration 206213, :track/name "Any Colour You Like", :track/position 8, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121286, :track/duration 226933, :track/name "Brain Damage", :track/position 9, :track/artists [{:db/id 17592186046909}]} {:db/id 17592186121287, :track/duration 131546, :track/name "Eclipse", :track/position 10, :track/artists [{:db/id 17592186046909}]}]}]}

;; noncomponent defaults (same example as "reverse lookup")
(d/pull db [:artist/_country] :country/GB)
	;;; ⇒ {:artist/_country [{:db/id 17592186045758} {:db/id 17592186045759} {:db/id 17592186045765} {:db/id 17592186045766} {:db/id 17592186045779} {:db/id 17592186045809} {:db/id 17592186045829} {:db/id 17592186045837} {:db/id 17592186045842} {:db/id 17592186045855} {:db/id 17592186045859} {:db/id 17592186045864} {:db/id 17592186045867} {:db/id 17592186045870} {:db/id 17592186045880} {:db/id 17592186045881} {:db/id 17592186045888} {:db/id 17592186045922} {:db/id 17592186045925} {:db/id 17592186045942} {:db/id 17592186045965} {:db/id 17592186045973} {:db/id 17592186045994} {:db/id 17592186045997} {:db/id 17592186046001} {:db/id 17592186046002} {:db/id 17592186046005} {:db/id 17592186046015} {:db/id 17592186046018} {:db/id 17592186046045} {:db/id 17592186046047} {:db/id 17592186046053} {:db/id 17592186046076} {:db/id 17592186046081} {:db/id 17592186046092} {:db/id 17592186046132} {:db/id 17592186046134} {:db/id 17592186046135} {:db/id 17592186046149} {:db/id 17592186046160} {:db/id 17592186046169} {:db/id 17592186046171} {:db/id 17592186046184} {:db/id 17592186046188} {:db/id 17592186046193} {:db/id 17592186046199} {:db/id 17592186046207} {:db/id 17592186046210} {:db/id 17592186046218} {:db/id 17592186046220} {:db/id 17592186046225} {:db/id 17592186046228} {:db/id 17592186046247} {:db/id 17592186046252} {:db/id 17592186046276} {:db/id 17592186046288} {:db/id 17592186046289} {:db/id 17592186046298} {:db/id 17592186046310} {:db/id 17592186046311} {:db/id 17592186046313} {:db/id 17592186046328} {:db/id 17592186046332} {:db/id 17592186046348} {:db/id 17592186046357} {:db/id 17592186046379} {:db/id 17592186046384} {:db/id 17592186046385} {:db/id 17592186046390} {:db/id 17592186046411} {:db/id 17592186046417} {:db/id 17592186046421} {:db/id 17592186046437} {:db/id 17592186046438} {:db/id 17592186046439} {:db/id 17592186046444} {:db/id 17592186046445} {:db/id 17592186046467} {:db/id 17592186046473} {:db/id 17592186046486} {:db/id 17592186046494} {:db/id 17592186046505} {:db/id 17592186046512} {:db/id 17592186046520} {:db/id 17592186046557} {:db/id 17592186046560} {:db/id 17592186046561} {:db/id 17592186046596} {:db/id 17592186046610} {:db/id 17592186046626} {:db/id 17592186046643} {:db/id 17592186046650} {:db/id 17592186046659} {:db/id 17592186046662} {:db/id 17592186046667} {:db/id 17592186046674} {:db/id 17592186046693} {:db/id 17592186046694} {:db/id 17592186046695} {:db/id 17592186046711} ...]}

;; reverse component lookup
(d/pull db [:release/_media] dylan-harrison-cd)
	;;; ⇒ {:release/_media {:db/id 17592186063798}}

;; map specifications
(d/pull db [:track/name {:track/artists [:db/id :artist/name]}] ghost-riders)
	;;; ⇒ {:track/name "Ghost Riders in the Sky", :track/artists [{:db/id 17592186048186, :artist/name "Bob Dylan"} {:db/id 17592186049854, :artist/name "George Harrison"}]}

concert-for-bangla-desh

;; nested map specifications
(d/pull db
        [{:release/media
          [{:medium/tracks
            [:track/name {:track/artists [:artist/name]}]}]}]
        concert-for-bangla-desh)
	;;; ⇒ {:release/media [{:medium/tracks [{:track/name "George Harrison / Ravi Shankar Introduction", :track/artists [{:artist/name "Ravi Shankar"} {:artist/name "George Harrison"}]} {:track/name "Bangla Dhun", :track/artists [{:artist/name "Ravi Shankar"}]}]} {:medium/tracks [{:track/name "Wah-Wah", :track/artists [{:artist/name "George Harrison"}]} {:track/name "My Sweet Lord", :track/artists [{:artist/name "George Harrison"}]} {:track/name "Awaiting on You All", :track/artists [{:artist/name "George Harrison"}]} {:track/name "That's the Way God Planned It", :track/artists [{:artist/name "Billy Preston"}]}]} {:medium/tracks [{:track/name "It Don't Come Easy", :track/artists [{:artist/name "Ringo Starr"}]} {:track/name "Beware of Darkness", :track/artists [{:artist/name "George Harrison"}]} {:track/name "Introduction of the Band", :track/artists [{:artist/name "George Harrison"}]} {:track/name "While My Guitar Gently Weeps", :track/artists [{:artist/name "George Harrison"}]}]} {:medium/tracks [{:track/name "Jumpin' Jack Flash / Youngblood", :track/artists [{:artist/name "Leon Russell"}]} {:track/name "Here Comes the Sun", :track/artists [{:artist/name "George Harrison"}]}]} {:medium/tracks [{:track/name "A Hard Rain's Gonna Fall", :track/artists [{:artist/name "Bob Dylan"}]} {:track/name "It Takes a Lot to Laugh / It Takes a Train to Cry", :track/artists [{:artist/name "Bob Dylan"}]} {:track/name "Blowin' in the Wind", :track/artists [{:artist/name "Bob Dylan"}]} {:track/name "Mr. Tambourine Man", :track/artists [{:artist/name "Bob Dylan"}]} {:track/name "Just Like a Woman", :track/artists [{:artist/name "Bob Dylan"}]}]} {:medium/tracks [{:track/name "Something", :track/artists [{:artist/name "George Harrison"}]} {:track/name "Bangla Desh", :track/artists [{:artist/name "George Harrison"}]}]}]}

;; wildcard specification
(d/pull db '[*] concert-for-bangla-desh)

;; wildcard + map specification
(d/pull db '[* {:track/artists [:artist/name]}] ghost-riders)

;; default expression
(d/pull db '[:artist/name (default :artist/endYear 0)] mccartney)

;; default expression with different type
(d/pull db '[:artist/name (default :artist/endYear "N/A")] mccartney)

;; absent attributes are omitted from results
(d/pull db '[:artist/name :died-in-1966?] mccartney)

;; explicit limit
(d/pull db '[(limit :track/_artists 10)] led-zeppelin)

;; limit + subspec
(d/pull db '[{(limit :track/_artists 10) [:track/name]}]
        led-zeppelin)

;; no limit
(d/pull db '[(limit :track/_artists nil)] led-zeppelin)

;; empty results
(d/pull db '[:penguins] led-zeppelin)

;; empty results in a collection
(d/pull db '[{:track/artists [:penguins]}] ghost-riders)


;; Examples below follow http://docs.datomic.com/query.html#pull

;; pull expression in query
(d/q '[:find [(pull ?e [:release/name]) ...]
       :in $ ?artist
       :where [?e :release/artists ?artist]]
     db
     led-zeppelin)

;; dynamic pattern input
(d/q '[:find [(pull ?e pattern) ...]
       :in $ ?artist pattern
       :where [?e :release/artists ?artist]]
     db
     led-zeppelin
     [:release/name])

