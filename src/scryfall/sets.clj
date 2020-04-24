(ns scryfall.sets
  (:require [scryfall.http :refer [call-scryfall]]))

(defn by-name [set-name & [is-tcg?]] (call-scryfall (str (when is-tcg? "tcgplayer/") set-name)))
