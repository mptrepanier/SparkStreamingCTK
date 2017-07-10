/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dia.red.ctakes.spark;

import java.util.List;
import java.io.PrintWriter;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import org.json.JSONObject;
//import org.apache.spark.streaming.Duration;
//import org.apache.spark.streaming.api.java.JavaDStream;
//import org.apache.spark.streaming.api.java.JavaStreamingContext;


/**
 * @author Selina Chu, Michael Starch, and Giuseppe Totaro
 *
 */
public class CtakesSparkMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {


		SparkConf conf = new SparkConf().setAppName("ctakes");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile("/mnt/d/metistream/ctakes-streaming/SparkStreamingCTK/testdata100.txt").map(new CtakesFunction());

        String first = lines.take(2).get(0);
        PrintWriter out = new PrintWriter("/mnt/d/metistream/ctakes-streaming/SparkStreamingCTK/test_outputs/output.txt");
        out.println(first);
        out.close();
        sc.close();

    }

}
