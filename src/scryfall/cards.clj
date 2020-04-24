(ns scryfall.cards
  (:require [scryfall.http :refer [call-scryfall]]))

(defn search [card-name & [params]] (call-scryfall "cards/search" (assoc params :q card-name)))

(defn paged
  ([] (paged 1))
  ([page] (call-scryfall (str "cards?page=" page))))

(defn named
  [card-name & [{:keys [fuzzy?] :as params}]]
  (call-scryfall "cards/named" (assoc params (if fuzzy? :fuzzy :exact) card-name)))

(defn autocomplete [q & [params]] (call-scryfall "cards/autocomplete" (assoc params :q q)))

(defn random [& [params]] (call-scryfall "cards/random" params))

(defn by-id
  [id & [{:keys [source set-code collector-number lang] :as params}]]
  (let [url (cond
              (and set-code collector-number) (str "cards/" set-code "/" collector-number (when lang (str "/" lang)))
              (or (not source) (= source :scryfall)) (str "cards/" id)
              :else (str "cards/" (name source) "/" id))]
    (call-scryfall url (dissoc params :source :set-code :collector-number :lang))))

(defn collection [params] (call-scryfall "cards/collection" (assoc params :method :post)))
