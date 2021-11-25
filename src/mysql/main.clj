(ns mysql.main
  (:require
    [mysql.db :as db])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn -main [& args]
  (println "**** in main!")
  (clojure.pprint/pprint (take 10 (db/get-books))))
  ;(clojure.pprint/pprint (take 10 (db/get-reviews-rf-all))))