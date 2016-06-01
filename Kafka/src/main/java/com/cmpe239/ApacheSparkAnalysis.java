package com.cmpe239;

import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.google.common.collect.Lists;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

//ApacheSparkAnalysis will send messages to apache spark
public class ApacheSparkAnalysis {
	private static final Pattern BLANK_SPACE = Pattern.compile("");

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Enter Arguments ");
			System.exit(1);
		}

		SparkConf configurationSpark = new SparkConf().setAppName("ApacheSparkAnalysis").setMaster("local");
		JavaStreamingContext contextStream = new JavaStreamingContext(configurationSpark, Durations.seconds(1));

		Set<String> listOfTopics = new HashSet<String>(Arrays.asList(args[1].split(",")));

		Map<String, String> params = new HashMap<String, String>();
		params.put("metadata.broker.list", args[0]);

		JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(contextStream, String.class,
				String.class, StringDecoder.class, StringDecoder.class, params, listOfTopics);

		JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
			public String call(Tuple2<String, String> tuple2) {
				return tuple2._2();
			}
		});

		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
			public Iterable<String> call(String inputStr) {
				return Lists.newArrayList(BLANK_SPACE.split(inputStr));
			}
		});

		JavaPairDStream<String, Integer> integerPrint = words.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String str) {
				return new Tuple2<String, Integer>(str, 1);
			}
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer int1, Integer int2) {
				return int1 + int2;
			}
		});

		integerPrint.print();
		contextStream.start();
		contextStream.awaitTermination();

	}

}
