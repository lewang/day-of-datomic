(ns tutorial
  (:require [datomic.api :as d]
            [datomic.samples.repl :as repl]))
	;;; ⇒ nil

;; working through tutorial - http://docs.datomic.com/tutorial.html



(def conn (repl/scratch-conn))
	;;; ⇒ #'tutorial/conn

(d/transact
 conn
 [{:db/ident :red}
  {:db/ident :green}
  {:db/ident :blue}
  {:db/ident :yellow}])
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@169280d8, :db-after datomic.db.Db@6db0d71e, :tx-data [#datom[13194139534312 50 #inst "2017-03-06T00:35:32.511-00:00" 13194139534312 true] #datom[17592186045417 10 :red 13194139534312 true] #datom[17592186045418 10 :green 13194139534312 true] #datom[17592186045419 10 :blue 13194139534312 true] #datom[17592186045420 10 :yellow 13194139534312 true]], :tempids {-9223301668109598084 17592186045417, -9223301668109598083 17592186045418, -9223301668109598082 17592186045419, -9223301668109598081 17592186045420}}} 0x30dff406]

(defn make-idents
  [x]
  (mapv #(hash-map :db/ident %) x))
	;;; ⇒ #'tutorial/make-idents

(def sizes [:small :medium :large :xlarge])
	;;; ⇒ #'tutorial/sizes

(def types [:shirt :pants :dress :hat])
	;;; ⇒ #'tutorial/types

(def colors [:red :green :blue :yellow])
	;;; ⇒ #'tutorial/colors

(d/transact conn (make-idents sizes))
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@6db0d71e, :db-after datomic.db.Db@fa3f6d36, :tx-data [#datom[13194139534317 50 #inst "2017-03-06T00:35:32.514-00:00" 13194139534317 true] #datom[17592186045422 10 :small 13194139534317 true] #datom[17592186045423 10 :medium 13194139534317 true] #datom[17592186045424 10 :large 13194139534317 true] #datom[17592186045425 10 :xlarge 13194139534317 true]], :tempids {-9223301668109598080 17592186045422, -9223301668109598079 17592186045423, -9223301668109598078 17592186045424, -9223301668109598077 17592186045425}}} 0x1ea75cef]

(d/transact conn (make-idents types))
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@fa3f6d36, :db-after datomic.db.Db@36d34405, :tx-data [#datom[13194139534322 50 #inst "2017-03-06T00:35:32.516-00:00" 13194139534322 true] #datom[17592186045427 10 :shirt 13194139534322 true] #datom[17592186045428 10 :pants 13194139534322 true] #datom[17592186045429 10 :dress 13194139534322 true] #datom[17592186045430 10 :hat 13194139534322 true]], :tempids {-9223301668109598076 17592186045427, -9223301668109598075 17592186045428, -9223301668109598074 17592186045429, -9223301668109598073 17592186045430}}} 0x3ee6e24d]


(def schema-1
  [{:db/ident :inv/sku
    :db/valueType :db.type/string
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   {:db/ident :inv/color
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :inv/size
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :inv/type
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}])
	;;; ⇒ #'tutorial/schema-1


(d/transact conn schema-1)
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@36d34405, :db-after datomic.db.Db@dbc266f9, :tx-data [#datom[13194139534327 50 #inst "2017-03-06T00:36:24.365-00:00" 13194139534327 true] #datom[63 10 :inv/sku 13194139534327 true] #datom[63 40 23 13194139534327 true] #datom[63 42 38 13194139534327 true] #datom[63 41 35 13194139534327 true] #datom[64 10 :inv/color 13194139534327 true] #datom[64 40 20 13194139534327 true] #datom[64 41 35 13194139534327 true] #datom[65 10 :inv/size 13194139534327 true] #datom[65 40 20 13194139534327 true] #datom[65 41 35 13194139534327 true] #datom[66 10 :inv/type 13194139534327 true] #datom[66 40 20 13194139534327 true] #datom[66 41 35 13194139534327 true] #datom[0 13 65 13194139534327 true] #datom[0 13 64 13194139534327 true] #datom[0 13 66 13194139534327 true] #datom[0 13 63 13194139534327 true]], :tempids {-9223301668109598072 63, -9223301668109598071 64, -9223301668109598070 65, -9223301668109598069 66}}} 0x6e333b1c]

(def sample-data
  (->> (for [color colors size sizes type types]
         {:inv/color color
          :inv/size size
          :inv/type type})
       (map-indexed
        (fn [idx map]
          (assoc map :inv/sku (str "SKU-" idx))))
       vec))
	;;; ⇒ #'tutorial/sample-data

sample-data
	;;; ⇒ [{:inv/color :red, :inv/size :small, :inv/type :shirt, :inv/sku "SKU-0"} {:inv/color :red, :inv/size :small, :inv/type :pants, :inv/sku "SKU-1"} {:inv/color :red, :inv/size :small, :inv/type :dress, :inv/sku "SKU-2"} {:inv/color :red, :inv/size :small, :inv/type :hat, :inv/sku "SKU-3"} {:inv/color :red, :inv/size :medium, :inv/type :shirt, :inv/sku "SKU-4"} {:inv/color :red, :inv/size :medium, :inv/type :pants, :inv/sku "SKU-5"} {:inv/color :red, :inv/size :medium, :inv/type :dress, :inv/sku "SKU-6"} {:inv/color :red, :inv/size :medium, :inv/type :hat, :inv/sku "SKU-7"} {:inv/color :red, :inv/size :large, :inv/type :shirt, :inv/sku "SKU-8"} {:inv/color :red, :inv/size :large, :inv/type :pants, :inv/sku "SKU-9"} {:inv/color :red, :inv/size :large, :inv/type :dress, :inv/sku "SKU-10"} {:inv/color :red, :inv/size :large, :inv/type :hat, :inv/sku "SKU-11"} {:inv/color :red, :inv/size :xlarge, :inv/type :shirt, :inv/sku "SKU-12"} {:inv/color :red, :inv/size :xlarge, :inv/type :pants, :inv/sku "SKU-13"} {:inv/color :red, :inv/size :xlarge, :inv/type :dress, :inv/sku "SKU-14"} {:inv/color :red, :inv/size :xlarge, :inv/type :hat, :inv/sku "SKU-15"} {:inv/color :green, :inv/size :small, :inv/type :shirt, :inv/sku "SKU-16"} {:inv/color :green, :inv/size :small, :inv/type :pants, :inv/sku "SKU-17"} {:inv/color :green, :inv/size :small, :inv/type :dress, :inv/sku "SKU-18"} {:inv/color :green, :inv/size :small, :inv/type :hat, :inv/sku "SKU-19"} {:inv/color :green, :inv/size :medium, :inv/type :shirt, :inv/sku "SKU-20"} {:inv/color :green, :inv/size :medium, :inv/type :pants, :inv/sku "SKU-21"} {:inv/color :green, :inv/size :medium, :inv/type :dress, :inv/sku "SKU-22"} {:inv/color :green, :inv/size :medium, :inv/type :hat, :inv/sku "SKU-23"} {:inv/color :green, :inv/size :large, :inv/type :shirt, :inv/sku "SKU-24"} {:inv/color :green, :inv/size :large, :inv/type :pants, :inv/sku "SKU-25"} {:inv/color :green, :inv/size :large, :inv/type :dress, :inv/sku "SKU-26"} {:inv/color :green, :inv/size :large, :inv/type :hat, :inv/sku "SKU-27"} {:inv/color :green, :inv/size :xlarge, :inv/type :shirt, :inv/sku "SKU-28"} {:inv/color :green, :inv/size :xlarge, :inv/type :pants, :inv/sku "SKU-29"} {:inv/color :green, :inv/size :xlarge, :inv/type :dress, :inv/sku "SKU-30"} {:inv/color :green, :inv/size :xlarge, :inv/type :hat, :inv/sku "SKU-31"} {:inv/color :blue, :inv/size :small, :inv/type :shirt, :inv/sku "SKU-32"} {:inv/color :blue, :inv/size :small, :inv/type :pants, :inv/sku "SKU-33"} {:inv/color :blue, :inv/size :small, :inv/type :dress, :inv/sku "SKU-34"} {:inv/color :blue, :inv/size :small, :inv/type :hat, :inv/sku "SKU-35"} {:inv/color :blue, :inv/size :medium, :inv/type :shirt, :inv/sku "SKU-36"} {:inv/color :blue, :inv/size :medium, :inv/type :pants, :inv/sku "SKU-37"} {:inv/color :blue, :inv/size :medium, :inv/type :dress, :inv/sku "SKU-38"} {:inv/color :blue, :inv/size :medium, :inv/type :hat, :inv/sku "SKU-39"} {:inv/color :blue, :inv/size :large, :inv/type :shirt, :inv/sku "SKU-40"} {:inv/color :blue, :inv/size :large, :inv/type :pants, :inv/sku "SKU-41"} {:inv/color :blue, :inv/size :large, :inv/type :dress, :inv/sku "SKU-42"} {:inv/color :blue, :inv/size :large, :inv/type :hat, :inv/sku "SKU-43"} {:inv/color :blue, :inv/size :xlarge, :inv/type :shirt, :inv/sku "SKU-44"} {:inv/color :blue, :inv/size :xlarge, :inv/type :pants, :inv/sku "SKU-45"} {:inv/color :blue, :inv/size :xlarge, :inv/type :dress, :inv/sku "SKU-46"} {:inv/color :blue, :inv/size :xlarge, :inv/type :hat, :inv/sku "SKU-47"} {:inv/color :yellow, :inv/size :small, :inv/type :shirt, :inv/sku "SKU-48"} {:inv/color :yellow, :inv/size :small, :inv/type :pants, :inv/sku "SKU-49"} {:inv/color :yellow, :inv/size :small, :inv/type :dress, :inv/sku "SKU-50"} {:inv/color :yellow, :inv/size :small, :inv/type :hat, :inv/sku "SKU-51"} {:inv/color :yellow, :inv/size :medium, :inv/type :shirt, :inv/sku "SKU-52"} {:inv/color :yellow, :inv/size :medium, :inv/type :pants, :inv/sku "SKU-53"} {:inv/color :yellow, :inv/size :medium, :inv/type :dress, :inv/sku "SKU-54"} {:inv/color :yellow, :inv/size :medium, :inv/type :hat, :inv/sku "SKU-55"} {:inv/color :yellow, :inv/size :large, :inv/type :shirt, :inv/sku "SKU-56"} {:inv/color :yellow, :inv/size :large, :inv/type :pants, :inv/sku "SKU-57"} {:inv/color :yellow, :inv/size :large, :inv/type :dress, :inv/sku "SKU-58"} {:inv/color :yellow, :inv/size :large, :inv/type :hat, :inv/sku "SKU-59"} {:inv/color :yellow, :inv/size :xlarge, :inv/type :shirt, :inv/sku "SKU-60"} {:inv/color :yellow, :inv/size :xlarge, :inv/type :pants, :inv/sku "SKU-61"} {:inv/color :yellow, :inv/size :xlarge, :inv/type :dress, :inv/sku "SKU-62"} {:inv/color :yellow, :inv/size :xlarge, :inv/type :hat, :inv/sku "SKU-63"}]

(d/transact conn sample-data)
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@dbc266f9, :db-after datomic.db.Db@6506bb2a, :tx-data [#datom[13194139534328 50 #inst "2017-03-06T00:37:33.395-00:00" 13194139534328 true] #datom[17592186045433 64 17592186045417 13194139534328 true] #datom[17592186045433 65 17592186045422 13194139534328 true] #datom[17592186045433 66 17592186045427 13194139534328 true] #datom[17592186045433 63 "SKU-0" 13194139534328 true] #datom[17592186045434 64 17592186045417 13194139534328 true] #datom[17592186045434 65 17592186045422 13194139534328 true] #datom[17592186045434 66 17592186045428 13194139534328 true] #datom[17592186045434 63 "SKU-1" 13194139534328 true] #datom[17592186045435 64 17592186045417 13194139534328 true] #datom[17592186045435 65 17592186045422 13194139534328 true] #datom[17592186045435 66 17592186045429 13194139534328 true] #datom[17592186045435 63 "SKU-2" 13194139534328 true] #datom[17592186045436 64 17592186045417 13194139534328 true] #datom[17592186045436 65 17592186045422 13194139534328 true] #datom[17592186045436 66 17592186045430 13194139534328 true] #datom[17592186045436 63 "SKU-3" 13194139534328 true] #datom[17592186045437 64 17592186045417 13194139534328 true] #datom[17592186045437 65 17592186045423 13194139534328 true] #datom[17592186045437 66 17592186045427 13194139534328 true] #datom[17592186045437 63 "SKU-4" 13194139534328 true] #datom[17592186045438 64 17592186045417 13194139534328 true] #datom[17592186045438 65 17592186045423 13194139534328 true] #datom[17592186045438 66 17592186045428 13194139534328 true] #datom[17592186045438 63 "SKU-5" 13194139534328 true] #datom[17592186045439 64 17592186045417 13194139534328 true] #datom[17592186045439 65 17592186045423 13194139534328 true] #datom[17592186045439 66 17592186045429 13194139534328 true] #datom[17592186045439 63 "SKU-6" 13194139534328 true] #datom[17592186045440 64 17592186045417 13194139534328 true] #datom[17592186045440 65 17592186045423 13194139534328 true] #datom[17592186045440 66 17592186045430 13194139534328 true] #datom[17592186045440 63 "SKU-7" 13194139534328 true] #datom[17592186045441 64 17592186045417 13194139534328 true] #datom[17592186045441 65 17592186045424 13194139534328 true] #datom[17592186045441 66 17592186045427 13194139534328 true] #datom[17592186045441 63 "SKU-8" 13194139534328 true] #datom[17592186045442 64 17592186045417 13194139534328 true] #datom[17592186045442 65 17592186045424 13194139534328 true] #datom[17592186045442 66 17592186045428 13194139534328 true] #datom[17592186045442 63 "SKU-9" 13194139534328 true] #datom[17592186045443 64 17592186045417 13194139534328 true] #datom[17592186045443 65 17592186045424 13194139534328 true] #datom[17592186045443 66 17592186045429 13194139534328 true] #datom[17592186045443 63 "SKU-10" 13194139534328 true] #datom[17592186045444 64 17592186045417 13194139534328 true] #datom[17592186045444 65 17592186045424 13194139534328 true] #datom[17592186045444 66 17592186045430 13194139534328 true] #datom[17592186045444 63 "SKU-11" 13194139534328 true] #datom[17592186045445 64 17592186045417 13194139534328 true] #datom[17592186045445 65 17592186045425 13194139534328 true] #datom[17592186045445 66 17592186045427 13194139534328 true] #datom[17592186045445 63 "SKU-12" 13194139534328 true] #datom[17592186045446 64 17592186045417 13194139534328 true] #datom[17592186045446 65 17592186045425 13194139534328 true] #datom[17592186045446 66 17592186045428 13194139534328 true] #datom[17592186045446 63 "SKU-13" 13194139534328 true] #datom[17592186045447 64 17592186045417 13194139534328 true] #datom[17592186045447 65 17592186045425 13194139534328 true] #datom[17592186045447 66 17592186045429 13194139534328 true] #datom[17592186045447 63 "SKU-14" 13194139534328 true] #datom[17592186045448 64 17592186045417 13194139534328 true] #datom[17592186045448 65 17592186045425 13194139534328 true] #datom[17592186045448 66 17592186045430 13194139534328 true] #datom[17592186045448 63 "SKU-15" 13194139534328 true] #datom[17592186045449 64 17592186045418 13194139534328 true] #datom[17592186045449 65 17592186045422 13194139534328 true] #datom[17592186045449 66 17592186045427 13194139534328 true] #datom[17592186045449 63 "SKU-16" 13194139534328 true] #datom[17592186045450 64 17592186045418 13194139534328 true] #datom[17592186045450 65 17592186045422 13194139534328 true] #datom[17592186045450 66 17592186045428 13194139534328 true] #datom[17592186045450 63 "SKU-17" 13194139534328 true] #datom[17592186045451 64 17592186045418 13194139534328 true] #datom[17592186045451 65 17592186045422 13194139534328 true] #datom[17592186045451 66 17592186045429 13194139534328 true] #datom[17592186045451 63 "SKU-18" 13194139534328 true] #datom[17592186045452 64 17592186045418 13194139534328 true] #datom[17592186045452 65 17592186045422 13194139534328 true] #datom[17592186045452 66 17592186045430 13194139534328 true] #datom[17592186045452 63 "SKU-19" 13194139534328 true] #datom[17592186045453 64 17592186045418 13194139534328 true] #datom[17592186045453 65 17592186045423 13194139534328 true] #datom[17592186045453 66 17592186045427 13194139534328 true] #datom[17592186045453 63 "SKU-20" 13194139534328 true] #datom[17592186045454 64 17592186045418 13194139534328 true] #datom[17592186045454 65 17592186045423 13194139534328 true] #datom[17592186045454 66 17592186045428 13194139534328 true] #datom[17592186045454 63 "SKU-21" 13194139534328 true] #datom[17592186045455 64 17592186045418 13194139534328 true] #datom[17592186045455 65 17592186045423 13194139534328 true] #datom[17592186045455 66 17592186045429 13194139534328 true] #datom[17592186045455 63 "SKU-22" 13194139534328 true] #datom[17592186045456 64 17592186045418 13194139534328 true] #datom[17592186045456 65 17592186045423 13194139534328 true] #datom[17592186045456 66 17592186045430 13194139534328 true] #datom[17592186045456 63 "SKU-23" 13194139534328 true] #datom[17592186045457 64 17592186045418 13194139534328 true] #datom[17592186045457 65 17592186045424 13194139534328 true] #datom[17592186045457 66 17592186045427 13194139534328 true] ...], :tempids {-9223301668109598025 17592186045476, -9223301668109598023 17592186045478, -9223301668109598051 17592186045450, -9223301668109598024 17592186045477, -9223301668109598065 17592186045436, -9223301668109598045 17592186045456, -9223301668109598020 17592186045481, -9223301668109598063 17592186045438, -9223301668109598049 17592186045452, -9223301668109598059 17592186045442, -9223301668109598036 17592186045465, -9223301668109598061 17592186045440, -9223301668109598062 17592186045439, -9223301668109598037 17592186045464, -9223301668109598018 17592186045483, -9223301668109598042 17592186045459, -9223301668109598005 17592186045496, -9223301668109598039 17592186045462, -9223301668109598066 17592186045435, -9223301668109598038 17592186045463, -9223301668109598048 17592186045453, -9223301668109598041 17592186045460, -9223301668109598034 17592186045467, -9223301668109598046 17592186045455, -9223301668109598056 17592186045445, -9223301668109598028 17592186045473, -9223301668109598067 17592186045434, -9223301668109598032 17592186045469, -9223301668109598054 17592186045447, -9223301668109598044 17592186045457, -9223301668109598022 17592186045479, -9223301668109598012 17592186045489, -9223301668109598011 17592186045490, -9223301668109598040 17592186045461, -9223301668109598047 17592186045454, -9223301668109598035 17592186045466, -9223301668109598064 17592186045437, -9223301668109598010 17592186045491, -9223301668109598015 17592186045486, -9223301668109598006 17592186045495, -9223301668109598026 17592186045475, -9223301668109598021 17592186045480, -9223301668109598055 17592186045446, -9223301668109598017 17592186045484, -9223301668109598019 17592186045482, -9223301668109598027 17592186045474, -9223301668109598033 17592186045468, -9223301668109598009 17592186045492, -9223301668109598060 17592186045441, -9223301668109598052 17592186045449, -9223301668109598014 17592186045487, -9223301668109598058 17592186045443, -9223301668109598057 17592186045444, -9223301668109598008 17592186045493, -9223301668109598013 17592186045488, -9223301668109598068 17592186045433, -9223301668109598030 17592186045471, -9223301668109598053 17592186045448, -9223301668109598029 17592186045472, -9223301668109598031 17592186045470, -9223301668109598050 17592186045451, -9223301668109598007 17592186045494, -9223301668109598043 17592186045458, -9223301668109598016 17592186045485}}} 0xbec7970]

(def db (d/db conn))
	;;; ⇒ #'tutorial/db

(d/pull db '[{:inv/color [:db/ident]}
             {:inv/size [:db/ident]}
             {:inv/type [:db/ident]}] [:inv/sku "SKU-42"])
	;;; ⇒ {:inv/color {:db/ident :blue}, :inv/size {:db/ident :large}, :inv/type {:db/ident :dress}}

;;;;;;;;;;;
;; query ;;
;;;;;;;;;;;

;;; same color as sku-42

(d/q '[:find ?sku
       :where
       [?e :inv/sku "SKU-42"]
       [?e :inv/color ?color]
       [?e2 :inv/color ?color]
       [?e2 :inv/sku ?sku]]
     db)
	;;; ⇒ #{["SKU-40"] ["SKU-32"] ["SKU-43"] ["SKU-44"] ["SKU-33"] ["SKU-41"] ["SKU-42"] ["SKU-36"] ["SKU-47"] ["SKU-37"] ["SKU-45"] ["SKU-34"] ["SKU-35"] ["SKU-46"] ["SKU-38"] ["SKU-39"]}





(def order-schema
  [{:db/ident :order/items
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true}
   {:db/ident :item/id
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :item/count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}])
	;;; ⇒ #'tutorial/order-schema

(d/transact conn order-schema)
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@6506bb2a, :db-after datomic.db.Db@c988634f, :tx-data [#datom[13194139534393 50 #inst "2017-03-06T00:44:15.456-00:00" 13194139534393 true] #datom[67 10 :order/items 13194139534393 true] #datom[67 40 20 13194139534393 true] #datom[67 41 36 13194139534393 true] #datom[67 43 true 13194139534393 true] #datom[68 10 :item/id 13194139534393 true] #datom[68 40 20 13194139534393 true] #datom[68 41 35 13194139534393 true] #datom[69 10 :item/count 13194139534393 true] #datom[69 40 22 13194139534393 true] #datom[69 41 35 13194139534393 true] #datom[0 13 69 13194139534393 true] #datom[0 13 68 13194139534393 true] #datom[0 13 67 13194139534393 true]], :tempids {-9223301668109598004 67, -9223301668109598003 68, -9223301668109598002 69}}} 0x542012c3]


(def add-order
  [{:order/items
    [{:item/id [:inv/sku "SKU-25"]
      :item/count 10}
     {:item/id [:inv/sku "SKU-26"]
      :item/count 20}]}])
	;;; ⇒ #'tutorial/add-order



(d/transact conn add-order)
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@c988634f, :db-after datomic.db.Db@38998153, :tx-data [#datom[13194139534394 50 #inst "2017-03-06T00:49:37.082-00:00" 13194139534394 true] #datom[17592186045499 67 17592186045500 13194139534394 true] #datom[17592186045500 68 17592186045458 13194139534394 true] #datom[17592186045500 69 10 13194139534394 true] #datom[17592186045499 67 17592186045501 13194139534394 true] #datom[17592186045501 68 17592186045459 13194139534394 true] #datom[17592186045501 69 20 13194139534394 true]], :tempids {-9223301668109598001 17592186045499, -9223301668109598000 17592186045500, -9223301668109597999 17592186045501}}} 0x2edfcc32]


(def db (d/db conn))
	;;; ⇒ #'tutorial/db

(d/q
 '[:find ?sku
   :in $ ?inv
   :where
   [?item :item/id ?inv]
   [?order :order/items ?item]
   [?order :order/items ?other-item]
   [?other-item :item/id ?other-inv]
   [?other-inv :inv/sku ?sku]]
 db
 [:inv/sku "SKU-25"])
	;;; ⇒ #{["SKU-25"] ["SKU-26"]}


(def rules
  '[[(ordered-together ?inv ?other-inv)
     [?item :item/id ?inv]
     [?order :order/items ?item]
     [?order :order/items ?other-item]
     [?other-item :item/id ?other-inv]]])
	;;; ⇒ #'tutorial/rules

(d/q
 '[:find ?sku
   :in $ % ?inv
   :where
   (ordered-together ?inv ?other-inv)
   [?other-inv :inv/sku ?sku]]
 db rules [:inv/sku "SKU-25"])
	;;; ⇒ #{["SKU-25"] ["SKU-26"]}


;;;;;;;;;;;;;
;; retract ;;
;;;;;;;;;;;;;


(def inventory-counts
  [{:db/ident :inv/count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}])
	;;; ⇒ #'tutorial/inventory-counts


(d/transact conn inventory-counts)
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@38998153, :db-after datomic.db.Db@4bf0d792, :tx-data [#datom[13194139534398 50 #inst "2017-03-06T02:18:08.648-00:00" 13194139534398 true] #datom[70 10 :inv/count 13194139534398 true] #datom[70 40 22 13194139534398 true] #datom[70 41 35 13194139534398 true] #datom[0 13 70 13194139534398 true]], :tempids {-9223301668109597998 70}}} 0xb908863]

(def inventory-update
  [[:db/add [:inv/sku "SKU-21"] :inv/count 7]
   [:db/add [:inv/sku "SKU-22"] :inv/count 7]
   [:db/add [:inv/sku "SKU-42"] :inv/count 100]])
	;;; ⇒ #'tutorial/inventory-update


(d/transact conn inventory-update)
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@4bf0d792, :db-after datomic.db.Db@e352f0e7, :tx-data [#datom[13194139534399 50 #inst "2017-03-06T02:22:06.690-00:00" 13194139534399 true] #datom[17592186045454 70 7 13194139534399 true] #datom[17592186045455 70 7 13194139534399 true] #datom[17592186045475 70 100 13194139534399 true]], :tempids {}}} 0x3c9ad29]


;;; explicit retract
(d/transact
 conn
 [[:db/retract [:inv/sku "SKU-22"] :inv/count 7]
  [:db/add "datomic.tx" :db/doc "remove incorrect assertion"]])
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@e352f0e7, :db-after datomic.db.Db@d58abc8e, :tx-data [#datom[13194139534400 50 #inst "2017-03-06T02:37:37.747-00:00" 13194139534400 true] #datom[17592186045455 70 7 13194139534400 false] #datom[13194139534400 62 "remove incorrect assertion" 13194139534400 true]], :tempids {"datomic.tx" 13194139534400}}} 0x70a609c5]

;;; implicit retract

(d/transact
 conn
 [[:db/add [:inv/sku "SKU-42"] :inv/count 1000]
  [:db/add "datomic.tx" :db/doc "correct data entry error"]])
	;;; ⇒ #datomic.promise/settable-future/reify--7008[{:status :ready, :val {:db-before datomic.db.Db@d58abc8e, :db-after datomic.db.Db@dfffa63e, :tx-data [#datom[13194139534401 50 #inst "2017-03-06T02:38:19.695-00:00" 13194139534401 true] #datom[17592186045475 70 1000 13194139534401 true] #datom[17592186045475 70 100 13194139534401 false] #datom[13194139534401 62 "correct data entry error" 13194139534401 true]], :tempids {"datomic.tx" 13194139534401}}} 0x44e1df93]

(def db (d/db conn))
	;;; ⇒ #'tutorial/db

(d/q
 '[:find ?sku ?count
   :where
   [?inv :inv/sku ?sku]
   [?inv :inv/count ?count]]
 db)
	;;; ⇒ #{["SKU-42" 1000] ["SKU-21" 7]}


;;;;;;;;;;;;;
;; history ;;
;;;;;;;;;;;;;


(def recent-tx
  (d/q
   '[:find (max 3 ?tx)
     :where
     [?tx :db/txInstant]]
   db))
	;;; ⇒ #'tutorial/recent-tx


(def txid (-> recent-tx ffirst last))
	;;; ⇒ #'tutorial/txid

(def db-before (d/as-of db txid))
	;;; ⇒ #'tutorial/db-before

(d/q
 '[:find ?sku ?count
   :where
   [?inv :inv/sku ?sku]
   [?inv :inv/count ?count]]
 db-before)
	;;; ⇒ #{["SKU-42" 100] ["SKU-21" 7] ["SKU-22" 7]}




(require '[clojure.pprint :as pp])
	;;; ⇒ nil

(def db-hist (d/history db))
	;;; ⇒ #'tutorial/db-hist

(->>
 (d/q
  '[:find ?tx ?sku ?val ?op
    :where
    [?inv :inv/count ?val ?tx ?op]
    [?inv :inv/sku ?sku]]
  db-hist)
 (sort-by first)
 (pp/pprint))
	;;; ⇒ nil
	;;; ⇒ <out>
	;;;   ([13194139534399 "SKU-21" 7 true]
	;;;    [13194139534399 "SKU-42" 100 true]
	;;;    [13194139534399 "SKU-22" 7 true]
	;;;    [13194139534400 "SKU-22" 7 false]
	;;;    [13194139534401 "SKU-42" 1000 true]
	;;;    [13194139534401 "SKU-42" 100 false])
