(ns scryfall.rulings
  (:require [scryfall.http :refer [call-scryfall]]))

(defn by-card-id
  [{:keys [card-id source set-code collector-number] :as params}]
  (let [uris (cond
              (and set-code collector-number) (str set-code "/" collector-number)
              (= source :scryfall) card-id
              :else (str (name source) "/" card-id))]
    (call-scryfall
      (str "cards/" uris "/rulings")
      (dissoc params :set-code :collector-number :provider :card-id))))

