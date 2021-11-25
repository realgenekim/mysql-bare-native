(ns mysql.db
  #:ghostwheel.core{:trace       true
                    :check       false
                    :num-tests   0
                    :outstrument true}
  (:require
    [config.core :refer [env]]
    [clojure.core.async :as async]
    [clojure.java.jdbc :as j]
    [clojure.spec.alpha :as s]
    [clojure.tools.logging :as log]
    [clj-time.jdbc]
    [clj-time.core :as t]
    [clj-time.format :as f]
    [clj-time.coerce :as tc]
    ;[expound.alpha :as expound]
    ;[korma.core :as k]
    ;[korma.db :as kdb]
    [honey.sql :as hsql]
    [honey.sql.helpers :as hsqlh]
    [mysql.time-utils :as tu]
    [com.fulcrologic.guardrails.noop :refer [>defn >def | ? =>]]))
    ;[ghostwheel.core :as g
    ; :refer [>defn >defn- >fdef => | <- ?]])

(set! *warn-on-reflection* true)

;(set! s/*explain-out* expound.alpha/printer)

; (config.core/reload-env) to reload config.edn in config/dev/config.edn


;; there are many ways to write a db-spec but the easiest way is to
;; use :dbtype and then provide the :dbname and any of :user, :password,
;; :host, :port, and other options as needed:
(def mysql-db {:dbtype   "mysql"
               :dbname   (:dbname (:db env))
               ;:host     (dbhost)
               :host     "127.0.0.1"
               :user     (:user (:db env))
               :password (:password (:db env))})


(defn get-books []
  (j/query mysql-db
    ["select * from books"]))




