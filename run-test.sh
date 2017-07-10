#!/usr/sh

mvn clean install;

#if [ ! -d target/spark-ctakes-0.1 ]; then
#	tar -zxzf target/spark-ctakes-0.1-dist.tar.gz -C target;
#fi
spark-submit  --class org.dia.red.ctakes.spark.CtakesSparkMain --conf spark.executor.extraClassPath=target/spark-ctakes-0.1.job --conf spark.driver.extraClassPath=target/spark-ctakes-0.1.job target/spark-ctakes-0.1-job.jar;