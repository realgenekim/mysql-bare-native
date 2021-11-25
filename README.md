The following works to create a runnable uberjar -- you'll have to change the user/password in src/mysql/db.clj.

- make uberjar
- make runuberjar

The following native build image fails -- the error message depends on what version of io.quarkus/quarkus-jdbc-mysql library I'm using.

- make native-image

(I started using io.quarkus/quarkus-jdbc-mysql in addition to mysql/mysql-connector-java after reading your post here: https://github.com/babashka/babashka/issues/372)

Many thanks!

Gene