(ns mysql.time-utils
  (:require
    [clj-time.local :as lt]
    [clj-time.coerce :as c]
    [com.fulcrologic.guardrails.noop :refer [>defn >def | ? =>]])
  (:import
    (org.ocpsoft.prettytime PrettyTime)
    ; old date style
    (java.util Date)
    (java.time.temporal ChronoUnit)
    (java.time LocalDateTime)
    (org.joda.time Duration)))

;https://github.com/fulcrologic/guardrails/blob/c04b6e5b7ee474b05eea99f482940573277a1118/src/main/com/fulcrologic/guardrails/noop.cljc

;(defmacro >defn
;  [& forms]
;  `(defn ~@forms))
;
;(def => :ret)


;
; time
;

(defn joda-datetime?
  [dt]
  (instance? java.time.LocalDateTime dt))

(def sample-dt
  {:nano 0,
   :hour 6,
   :second 27,
   :offset nil; #object[java.time.ZoneOffset 0x2d4d70c8 "Z"],
   :monthValue 11,
   :month nil; #object[java.time.Month 0x4c1beef0 "NOVEMBER"],
   :dayOfMonth 16,
   :year 2021,
   :class nil; java.time.OffsetDateTime,
   :dayOfWeek nil; #object[java.time.DayOfWeek 0x36442e23 "TUESDAY"],
   :dayOfYear 320,
   :minute 30})

(defn jodatimeutc->offsetdatetime
  " convert jodatime to java time OffsetDateTime (has no offsets / time zones)  --
    important because times coming out of mysql are UTC
    (LocalDateTime has no timezone) "
  [jodatm]
  (let [bjt (bean jodatm)
        javatm (try
                 (java.time.OffsetDateTime/of
                   (:year bjt) (:monthOfYear bjt) (:dayOfMonth bjt)
                   (:hourOfDay bjt) (:minuteOfHour bjt) (:secondOfMinute bjt)
                   0
                   (java.time.ZoneOffset/ofHours 0))
                 (catch Exception e
                   ;(log/error "   !!!!! xform-salesranks: cheshire/parse-string exception! " e)
                   nil))]
    (if (nil? javatm)
      (throw (Exception. (str "jodatimeutc->offsetdatetime: not jodatime: " jodatm)))
      javatm)))


(comment

  ; https://nextjournal.com/schmudde/java-time
  ; https://blog.joda.org/2014/11/converting-from-joda-time-to-javatime.html

  ;(def jt #clj-time/date-time"2021-11-16T06:30:27.000Z")
  (type jt)
  (def bjt (bean jt))
  (.year t)
  ; (type t)
  ;=> org.joda.time.DateTime

  (def dt (java.time.OffsetDateTime/now))
  (bean dt)
  (java.time.OffsetDateTime/now)
  (def dt (java.time.OffsetDateTime. "2021-11-16T06:30:27.000Z"))
  (java.time.OffsetDateTime/parse)
  ; https://stackoverflow.com/questions/28877981/how-to-convert-from-org-joda-time-datetime-to-java-time-zoneddatetime
  ; https://stackoverflow.com/questions/45348503/joda-time-to-java-8-conversion
  ; int year, int month, int dayOfMonth,
  ; int hour, int minute int second,
  ; int nanoOfSecond, ZoneOffset offset)
  ; https://docs.oracle.com/javase/8/docs/api/java/time/ZoneOffset.html
  (java.time.OffsetDateTime/of
    (:year bjt) (:monthOfYear bjt) (:dayOfMonth bjt)
    (:hourOfDay bjt) (:minuteOfHour bjt) (:secondOfMinute bjt)
    0
    (java.time.ZoneOffset/ofHours 0))

  (jodatimeutc->offsetdatetime jt)
  (def tt (jodatimeutc->offsetdatetime jt))
  (bean tt)

  (LocalDateTime/now)
  (.getHour (LocalDateTime/now))

  ,)


;
; time math
;

(>defn dt-minus-days
  " subtract n number of days "
  [dt n] [any? int? => any?]
  (.minusDays dt n))

(defn dt->millis
  " convert to milliseconds "
  [dt]
  (if (instance? java.time.LocalDateTime dt)
    (.toEpochMilli (.toInstant dt (java.time.ZoneOffset/of "Z")))
    (.toEpochMilli (.toInstant dt))))

(comment
  (def ldtnow (java.time.LocalDateTime/now))
  (type ldtnow)
  (instance? java.time.LocalDateTime ldtnow)
  (.toInstant ldtnow (java.time.ZoneOffset/of "Z"))
  ,)


(defn millis->days
  [ms]
  (let [days (/ ms 60 60 24 1000.0)]
    (Math/abs days)))

(defn millis->hours
  [ms]
  (let [hours (/ ms 60 60 1000.0)]
    (Math/abs hours)))

; PrettyTime for thigns like "3 min ago"

(def PRETTY-TIME (PrettyTime.))

(defn local-date-time>date
  " old style Date. takes as a constructor # of millis since epoch "
  [ldt]
  (Date. (dt->millis ldt)))




(comment

  ; https://www.ocpsoft.org/prettytime/
  ; System.out.println(p.format(new Date()));
  ;  => “moments ago”
  ; System.out.println(p.format(LocalDateTime.now().minusSeconds(1)));
  ;  => “moments ago”

  (ldt-ago (-> (java.time.OffsetDateTime/now)
             (.minus (jt/duration 10 :hours)))
    (java.time.OffsetDateTime/now))

  (comment
    (lt/local-now)
    ,)


  ,)

;
; address MySQL JDBC timezone problem
;



; global variable, because we use to get time offset once
(def local-time-offset-seconds
  (.getTotalSeconds (.getOffset (java.time.OffsetDateTime/now))))

(defn convert-zoned-clj-time-to-localdatetime
  [dt]
  (.plusSeconds (jodatimeutc->offsetdatetime dt)
    local-time-offset-seconds))


