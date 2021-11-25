The following works to create a runnable uberjar -- you'll have to change the user/password in src/mysql/db.clj.

- make uberjar
- make runuberjar

The following native build image fails -- the error message depends on what version of io.quarkus/quarkus-jdbc-mysql library I'm using.

- make native-image

(I started using io.quarkus/quarkus-jdbc-mysql in addition to mysql/mysql-connector-java after reading your post here: https://github.com/babashka/babashka/issues/372)

- no io.quarkus/quarkus-jdbc-mysql: https://0bin.net/paste/d7dek4bF#6ATWykAyyeyccQtfqC2-QjRt/sKTQs7zWneF8xTeROw
- io.quarkus/quarkus-jdbc-mysql: 1.13.7.Final: error message: https://0bin.net/paste/UEZqnCTn#DKo6QM+antsyZEsNBaxzZPp7KVhS3Zi1KeCt9LigP25
- io.quarkus/quarkus-jdbc-mysql: 1.13.7.Final: error message: https://0bin.net/paste/mTX0etdC#bKUUizjIY6qbIlyIFo3vwp2P9Gp6zgCWx27yelaRTzH
- io.quarkus/quarkus-jdbc-mysql: 2.4.2.final: error message: https://0bin.net/paste/ixPI6-17#T6dT5hWeKj17eJtnNFS69pEIl8t0a8nqAFsB5AdqxpE


Attempted to copy reflect.json and --initializate-at-run-time here:

- found list of reflections here: https://github.com/babashka/babashka-sql-pods/blob/master/reflection-mysql.json
- found list of run-time-initializations: https://github.com/babashka/babashka-sql-pods/blob/b12131df6454df98aafe9e0b0d89e7cb5919bb6a/script/compile

Many thanks!

Gene