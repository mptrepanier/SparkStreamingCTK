#!/usr/sh

mvn clean install;

<<<<<<< HEAD
#if [ ! -d target/spark-ctakes-0.1 ]; then
#	tar -zxzf target/spark-ctakes-0.1-dist.tar.gz -C target;
#fi
spark-submit  --class org.dia.red.ctakes.spark.CtakesSparkMain --conf spark.executor.extraClassPath=target/spark-ctakes-0.1.job --conf spark.driver.extraClassPath=target/spark-ctakes-0.1.job target/spark-ctakes-0.1-job.jar;
=======
if [ ! -d target/spark-ctakes-0.1 ]; then
	tar -zxzf target/spark-ctakes-0.1-dist.tar.gz -C target;
fi

target/spark-ctakes-0.1/bin/start-ctakes-server.sh;
>>>>>>> 676d6aa1378c778db9f9a6ce9815d35cfb95de9e
