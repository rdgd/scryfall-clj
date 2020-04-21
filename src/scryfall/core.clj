(ns scryfall.core
  (:require [clj-http.client :as http]
            [clojure.string :as cs]))
(defn map->params
  [m]
  (reduce-kv (fn [acc k v]
               (if (cs/blank? acc)
                 (str acc (name k) "=" v)
                 (str acc "&" (name k) "=" v))) "" m))

;;;; NEW
(defn call-scryfall
  [url & [{:keys [method] :as params}]]
  (let [http-fn (case method :post http/post http/get)]
    (try
      (-> (http-fn (str "https://api.scryfall.com/" url (when params (str "?" (map->params params)))) {:as :json})
          :body)
      (catch Exception e
        (throw (ex-info (str "Error occurred while calling " url (when params (str " with params " params)) e) (ex-data e)))))))

;;;; API
(defn set-data [set-name & [is-tcg?]] (call-scryfall (str (when is-tcg? "tcgplayer/") set-name)))

(defn cards-paged
  ([] (get-paged-cards 1))
  ([page] (call-scryfall (str "cards?page=" page))))

(defn cards-search [card-name & [params]] (call-scryfall "cards/search" (assoc params :q card-name)))

(defn cards-named
  [card-name & [{:keys [fuzzy?] :as params}]]
  (call-scryfall "cards/named" (assoc params (if fuzzy? :fuzzy :exact) card-name)))

(defn cards-autocomplete [q & [params]] (call-scryfall "cards/autocomplete" (assoc params :q q)))

(defn cards-random [params] (call-scryfall "cards/random" params))

(defn cards-collection [params] (call-scryfall "cards/collection" (assoc params :method :post)))

(defn cards-by-code-and-collector-number
  [code collector-number & [{:keys [lang] :as params}]]
  (call-scryfall (str "cards/" code "/" collector-number (when lang (str "/" lang))) params))

(defn card-by-multiverse-id
  [id & [params]]
  (call-scryfall (str "cards/multiverse/" id) params))

(defn card-by-mtgo-id
  [id & [params]]
  (call-scryfall (str "cards/mtgo/" id) params))

(defn card-by-arena-id
  [id & [params]]
  (call-scryfall (str "cards/arena/" id) params))

(defn card-by-tcgplayer-id
  [id & [params]]
  (call-scryfall (str "cards/tcgplayer/" id) params))

(defn card-by-id
  [id & [params]]
  (call-scryfall (str "cards/" id) params))

;;;; OLD
(defn get-card-data
  [card-name]
  (try (-> (http/get (str "https://api.scryfall.com/cards/named?fuzzy=" card-name) {:as :json})
           :body)
       (catch Exception e (println "Error searching for card " card-name " :" (ex-data e)))))

(defn get-card-image-url
  [card-name]
  (try (-> (get-card-data card-name)
           (get-in [:image_uris :large]))
       (catch Exception e (println "Error searching for card " card-name " :" (ex-data e)))))

(defn get-card-rulings
  [card-data]
  (try (-> card-data
           (get-in [:rulings_uri])
           (http/get {:as :json})
           :body
           :data)
       (catch Exception e (println "Error searching for rulings for " card-data " :" (ex-data e)))))

(defn get-legalities
  [card-data]
  (let [mk-strs (fn [l]
                  (str "**Legal in**: " (clojure.string/join ", " (get l "legal")) " \n**Illegal in**: "(clojure.string/join ", " (get l "not_legal")) ))]
    (->> (:legalities card-data)
         (reduce-kv (fn [acc k v] (update acc v (fn [x] (conj x (name k))))) {})
         mk-strs)))
;(get-card-rulings (get-card-data "nighthowler"))

(comment
  (def ayara (get-card-data "ayara first of locthwain"))
  (def krasis (get-card-data "nighthowler"))
  (reduce-kv (fn [acc k v] (update acc v (fn [x] (conj x (name k))))) {} (:legalities ayara))
  (get-legalities ayara)
  (:name krasis))
