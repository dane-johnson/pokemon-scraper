(ns pokemon-scraper.sql
  (:require [clojure.string :refer [join]])
  (:gen-class))

(defn element-reducer
  "Reduces the elements of a map into insert statements"
  [m]
  (str "("
       (join "," (map (fn [v]
                            (if (string? v) (str "'" v "'") (str v)))
                      (vals m)))
       ")"))

(defn make-insert-statement
  "Takes an array of map futures and creates an sql query to insert them into the table
  Note there is an assumption that the keywords in the map are correct columns in the 
  table"
  [ms table]
  (-> (str "INSERT INTO " table "\n")
      (str "(" (join "," (map name (keys (first ms)))) ")\n")
      (str "VALUES\n")
      (str (join ",\n" (map element-reducer ms)))
      (str ";")))
