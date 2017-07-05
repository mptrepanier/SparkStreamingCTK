#!/usr/sh

mvn clean install;

if [ ! -d target/spark-ctakes-0.1 ]; then
	tar -zxzf target/spark-ctakes-0.1-dist.tar.gz -C target;
fi

target/spark-ctakes-0.1/bin/start-ctakes-server.sh;