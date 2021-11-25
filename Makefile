uberjar:
	time clojure -X:uberjar :aot true :jar ./target/mysql-standalone.jar \
		:main-class mysql.main

runuberjar:
	# java -cp target/pubsub-standalone.jar clojure.main -m pubsub.core --mode get-salesranks
	GOOGLE_APPLICATION_CREDENTIALS=projectowner-BookTracker-53434d3aa596.json java -jar target/mysql-standalone.jar --mode get-salesranks

native-image:
	time native-image \
	         --report-unsupported-elements-at-runtime \
			 --no-fallback \
             -jar ./target/mysql-standalone.jar \
             -H:ReflectionConfigurationFiles=reflect-config.json \
             --allow-incomplete-classpath \
             -J-Dclojure.spec.skip-macros=true \
             --initialize-at-run-time=com.mysql.cj.jdbc.AbandonedConnectionCleanupThread \
             --initialize-at-run-time=com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.AbandonedConnectionCleanupThread \
             --initialize-at-run-time=com.mysql.cj.jdbc.Driver \
             --initialize-at-run-time=com.mysql.cj.jdbc.NonRegisteringDriver \
             -H:+ReportExceptionStackTraces \
             --initialize-at-build-time=. \

#              -H:+JNI \
#              --initialize-at-run-time=org.hibernate.jpa.HibernatePersistenceProvider \
#              --initialize-at-run-time=org.hibernate.secure.internal.StandardJaccServiceImpl \
#              --initialize-at-run-time=oracle.jdbc.driver.BlockSource \
#              --initialize-at-run-time=oracle.jdbc.driver.BlockSource$ThreadedCachingBlockSource$BlockReleaser \
#              --initialize-at-run-time=oracle.jdbc.driver.NamedTypeAccessor$XMLFactory \
#              --initialize-at-run-time=oracle.jdbc.driver.OracleTimeoutThreadPerVM \
#              --initialize-at-run-time=oracle.jdbc.driver.SQLUtil$XMLFactory \
#              --initialize-at-run-time=oracle.net.nt.TimeoutInterruptHandler \
#              --initialize-at-run-time=com.mysql.cj.jdbc.Driver --initialize-at-run-time=com.mysql.cj.jdbc.AbandonedConnectionCleanupThread --initialize-at-run-time=java.sql.DriverManager \

#              --initialize-at-build-time=com.fasterxml.jackson \
#              --initialize-at-build-time=clojure,cheshire \
#              --initialize-at-build-time=. \
#              --trace-object-instantiation=java.lang.Thread \

#
#              -H:ReflectionConfigurationFiles=reflect-config.json \

# https://github.com/oracle/graal/issues/1924
# https://github.com/quarkusio/quarkus/issues/1658#issuecomment-523618130
# --initialize-at-run-time=com.mysql.cj.jdbc.Driver --initialize-at-run-time=com.mysql.cj.jdbc.AbandonedConnectionCleanupThread --initialize-at-run-time=java.sql.DriverManager \

# https://github.com/babashka/babashka/issues/372
# found list of reflections here:
# https://github.com/babashka/babashka-sql-pods/blob/master/reflection-mysql.json
# list of run-time-initializations:
# https://github.com/babashka/babashka-sql-pods/blob/b12131df6454df98aafe9e0b0d89e7cb5919bb6a/script/compile

run-uberjar-with-agent:
# 	java -agentlib:native-image-agent=caller-filter-file=filter.json,config-output-dir=. -cp $(clojure -Spath):classes refl.main
	java -agentlib:native-image-agent=caller-filter-file=filter.json,config-output-dir=. -jar target/mysql-standalone.jar
