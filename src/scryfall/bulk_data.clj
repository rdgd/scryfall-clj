(ns scryfall.bulk-data
  (:require [scryfall.http :refer [call-scryfall]]))

(defn bulk-data
  [& [params]]
  (call-scryfall (str "bulk-data") params))


