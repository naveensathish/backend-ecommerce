package com.example.loginregister.config;

import java.util.HashMap;

import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

	private final String bootstrapServers = "localhost:9092";

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> config = new HashMap<>();
		config.put("bootstrap.servers", "localhost:9092");
		return new KafkaAdmin(config);
	}

	@Bean
	public NewTopic kcg1Topic() {
		return new NewTopic("kcg1", 1, (short) 1);
	}

	@Bean
	public NewTopic kcg2Topic() {
		return new NewTopic("kcg2", 1, (short) 1);
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put("bootstrap.servers", "localhost:9092");
		config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public NewTopic cartItemsTopic() {
		return new NewTopic("cart-items-topic", 1, (short) 1);
	}

	@Bean
	public NewTopic wishlistItemsTopic() {
		return new NewTopic("wishlist-items-topic", 1, (short) 1);
	}

	@Bean
	public NewTopic userLoginTopic() {
		return new NewTopic("user-login-topic", 1, (short) 1);
	}

	@Bean
	public NewTopic userRegistrationTopic() {
		return new NewTopic("user-registration-topic", 1, (short) 1);
	}

	@Bean
	public NewTopic productDisplayTopic() {
		return new NewTopic("product-display-topic", 1, (short) 1);
	}

	@Bean
	public NewTopic sellerLoginTopic() {
		return new NewTopic("seller-login-topic", 1, (short) 1);
	}

	@Bean
	public NewTopic sellerOnboardTopic() {
		return new NewTopic("seller-onboard-topic", 1, (short) 1);
	}

	@Bean
	public NewTopic productAdditionTopic() {
		return new NewTopic("product-addition-topic", 1, (short) 1);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		Map<String, Object> config = new HashMap<>();
		config.put("bootstrap.servers", "localhost:9092");
		config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(config);
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConsumerFactory<String, String> userLoginConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put("bootstrap.servers", "localhost:9092");
		config.put("group.id", "user-login-group");
		config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> userLoginKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(userLoginConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, String> productAdditionConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "product-addition-group");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> productAdditionkafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(productAdditionConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, String> userRegistrationConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put("bootstrap.servers", "localhost:9092");
		config.put("group.id", "user-registration-group");
		config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> userRegistrationKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(userRegistrationConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, String> cartConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put("bootstrap.servers", "localhost:9092");
		config.put("group.id", "cart-items-group");
		config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConsumerFactory<String, String> wishlistConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put("bootstrap.servers", "localhost:9092");
		config.put("group.id", "wishlist-items-group");
		config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConsumerFactory<String, String> productDisplayConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "product-display-group");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> productDisplayKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(productDisplayConsumerFactory());
		factory.setConcurrency(3);
		return factory;
	}

	@Bean
	public ConsumerFactory<String, String> sellerLoginConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "seller-login-group");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> sellerLoginKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(sellerLoginConsumerFactory());
		factory.setConcurrency(3);
		return factory;
	}

	@Bean
	public ConsumerFactory<String, String> sellerOnboardConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "seller-onboard-group");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> sellerOnboardKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(sellerOnboardConsumerFactory());
		factory.setConcurrency(3);
		return factory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> cartKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(cartConsumerFactory());
		return factory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> wishlistKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(wishlistConsumerFactory());
		return factory;
	}
}