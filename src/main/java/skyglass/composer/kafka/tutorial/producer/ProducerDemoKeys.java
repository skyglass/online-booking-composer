package skyglass.composer.kafka.tutorial.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import skyglass.composer.kafka.producer.ProducerDemo;

public class ProducerDemoKeys {

	private static Logger logger = LoggerFactory.getLogger(ProducerDemo.class);

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//create Producer properties
		String bootstrapServers = "127.0.0.1:9092";
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		//create the Producer
		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		for (int i = 0; i < 10; i++) {

			String topic = "first_topic";
			String value = "hello world" + Integer.toString(i);
			String key = "id_" + Integer.toString(i);
			//create a producer record
			ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

			logger.info("Key: " + key); //log the key
			//id_0 is going to partition 1
			//id_1 is partition 0
			//id_2 is partition 2
			//id_3 is partition 0
			//id_4 is partition 2
			//id_5 is partition 2
			//id_6 is partition 0
			//id_7 is partition 2
			//id_8 is partition 1
			//id_9 is partition 2

			//send data ~ asynchronous
			producer.send(record, new Callback() {

				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					//executes every time a record is successfully sent or an exception is thrown
					if (exception == null) {
						//the record was successfully sent
						logger.info("Received new metadata. \n" +
								"Topic: " + metadata.topic() + "\n" +
								"Partition: " + metadata.partition() + "\n" +
								"Offset: " + metadata.offset() + "\n" +
								"Timestamp: " + metadata.timestamp());
					} else {
						logger.error("Error while producing", exception);
					}

				}
			}).get(); //block the .send() to make it synchronous - don't do this in production
		}

		//flush data
		producer.flush();
		//close producer
		producer.close();

	}

}
