package com.cmpe239;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class ProducerKafkaMoonshot {

	public static void main(String[] args) {

		Properties configuration = new Properties();
		configuration.put("metadata.broker.list", "localhost:9092");
		configuration.put("serializer.class", "kafka.serializer.StringEncoder");
		configuration.put("request.required.acks", "1");
		Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(configuration));
		String topic = "cmpe239ApacheMoonshot";
		try {
			while (true) {
				MessageReciever messageReciever = new MessageReciever();
				String[] randomResponse = messageReciever.messageReciever();
				KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, randomResponse[0],
						randomResponse[1]);
				producer.send(data);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		finally
		{
			producer.close();
		}
	}

}