(ns scryfall.http
  (:require [clj-http.client :as http]))

(defn call-scryfall
  [url & [{:keys [method] :as params}]]
  (let [[http-fn params-key] (case method :post [http/post :form-params] [http/get :query-params])]
    (try
      (-> (http-fn (str "https://api.scryfall.com/" url) {:as :json params-key params})
          :body)
      (catch Exception e
        (throw (ex-info (str "Error occurred while calling " url (when params (str " with params " params)) e) (ex-data e)))))))
