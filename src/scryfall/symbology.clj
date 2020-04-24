(ns scryfall.symbology
  (:require [scryfall.http :refer [call-scryfall]]))

(defn symbology
  [& [params]]
  (call-scryfall "symbology" params))

(defn parse-mana
  [cost & [params]]
  (call-scryfall "symbology/parse-mana" (assoc params :cost cost)))
