(ns pokemon-scraper.core
  (:require [pokemon-scraper.scraper :refer [get-all-species]]
            [pokemon-scraper.sql :refer [make-insert-statement]])
  (:gen-class))

(defn -main
  [& args]
  (cond
    (= (first args) "species") (println (make-insert-statement (get-all-species) "species"))))
