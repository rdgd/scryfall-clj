(ns scryfall.catalog
  (:require [scryfall.http :refer [call-scryfall]]))

(defn catalog
  [catalog-name & [params]]
  (call-scryfall (str "catalog/" catalog-name) params))

