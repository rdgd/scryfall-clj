(ns scryfall.data-util
  (:require [clj-http.client :as http]))

(defn get-card-image-url
  [card-data & [size]]
  (get-in card-data [:image_uris (or size :large)]))

(defn get-card-rulings
  [card-data]
  (try (-> card-data
           (get-in [:rulings_uri])
           (http/get {:as :json})
           :body
           :data)
       (catch Exception e (println "Error searching for rulings for " card-data " :" (ex-data e)))))

(defn get-legalities-str
  [card-data]
  (let [mk-strs (fn [l]
                  (str "**Legal in**: " (clojure.string/join ", " (get l "legal")) " \n**Illegal in**: "(clojure.string/join ", " (get l "not_legal")) ))]
    (->> (:legalities card-data)
         (reduce-kv (fn [acc k v] (update acc v (fn [x] (conj x (name k))))) {})
         mk-strs)))
