package com.zekelabs.microserviceskafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.web.client.RestTemplate;

import Serializer.CustomSerializer;



@SpringBootApplication
@EnableEurekaClient
public class MicroServicesKafkaApplication {
	
	private final Logger logger = LoggerFactory.getLogger(MicroServicesKafkaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MicroServicesKafkaApplication.class, args);
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public KafkaTemplate<Object, Object> getKafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	public ProducerFactory<Object, Object> producerFactory() {
		// TODO Auto-generated method stub
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		//props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());
		//props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		//props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());
		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ConsumerFactory<Object, Object> kafkaConsumerFactory,
			KafkaTemplate<Object, Object> template) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		configurer.configure(factory, kafkaConsumerFactory);
		factory.setErrorHandler(new SeekToCurrentErrorHandler(
				new DeadLetterPublishingRecoverer(template), 3)); // dead-letter after 3 tries
		return factory;
	}

	
	@KafkaListener(id = "fooGroup", topics = "Thur")
	public void listen(String foo) {
		logger.info("Received: " + foo);
	}

}


