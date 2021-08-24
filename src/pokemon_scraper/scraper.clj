(ns pokemon-scraper.scraper
  (:require [cheshire.core :refer [parse-string]]
            [clj-http.client :as client])
  (:gen-class))

(def ^:dynamic *n-pokemon* 802)
(def ^:dynamic *api-url* "http://pokeapi.co/api/v2")

(def get-resource-map
  "Returns the requested resource as a map"
  (memoize (fn [res]
             (parse-string (:body (client/get (str *api-url* res))) keyword))))

(defn get-description
  "Get an english description of a pokemon"
  [n]
  ((comp :description first) (filter #(= "en" (get-in % [:language :name]))
                                     (:descriptions (get-resource-map (str "/characteristic/" n))))))

(defn get-species
  "Api call to get information on a species"
  [n]
  (as-> (get-resource-map (str "/pokemon/" n)) $
    ;; Description needs to be fetched independently
    (assoc $ :description (get-description n))
    ;; Type needs to be gleaned from a nested structure
    (assoc $ :type (:name (:type (first (:types $)))))
    ;; base attack and defense not in the table, randomly pick them
    (assoc $ :base-attack (rand-int 10) :base-defense (rand-int 10))
    (select-keys $ [:name :description :height :weight :order :type :base-attack :base-defense])))

(defn get-all-species
  "Returns all pokemon. Waits 30 secs between every 20 calls"
  []
  (loop [pokemon [] n 1] ;;Can't count directly, some are missing?
    (if (>= n *n-pokemon*)
      pokemon
      (do
        (Thread/sleep 30000)
        (recur (concat pokemon (doall (pmap get-species (range n (min (+ n 20) *n-pokemon*))))) (+ n 20))))))
