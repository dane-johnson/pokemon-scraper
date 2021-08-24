(ns pokemon-scraper.sql-test
  (:require [clojure.test :refer :all]
            [clojure.string :refer [join]]
            [pokemon-scraper.sql :refer :all]))

(deftest test-make-insert-statement
  (let [ms [{:name "rick" :fucks 0}
            {:name "morty" :fucks 1}]
        table "characters"]
    (is (= (make-insert-statement ms table)
           (join "\n" ["INSERT INTO characters"
                       "(name,fucks)"
                       "VALUES"
                       "('rick',0),"
                       "('morty',1);"])))))
