package com.poc.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;

@EnableKafka
@Configuration
public class KakfaConfiguration {
	
	private static final String ACKS_ALL = "all";
//	private static final String IO_CONFLUENT_MONITORING_PRODUCER_INTERCEPTOR = "io.confluent.monitoring.clients.interceptor.MonitoringProducerInterceptor";
	private static final String JKS = "JKS";
	private static final String SSL_TRUSTSTORE_TYPE = "ssl.truststore.type";
	private static final String SSL_TRUSTSTORE_PASSWORD = "ssl.truststore.password";
	private static final String SSL_TRUSTSTORE_LOCATION = "ssl.truststore.location";
	private static final String SSL = "SSL";
	private static final String SECURITY_PROTOCOL = "security.protocol";
	private static final String CONFLUENT_MONITORING_INTERCEPTOR_SSL_TRUSTSTORE_PASSWORD = "confluent.monitoring.interceptor.ssl.truststore.password";
	private static final String CONFLUENT_MONITORING_INTERCEPTOR_SSL_TRUSTSTORE_LOCATION = "confluent.monitoring.interceptor.ssl.truststore.location";
	private static final String CONFLUENT_MONITORING_INTERCEPTOR_SECURITY_PROTOCOL = "confluent.monitoring.interceptor.security.protocol";
	private static final String CONFLUENT_MONITORING_INTERCEPTOR_BOOTSTRAP_SERVERS = "confluent.monitoring.interceptor.bootstrap.servers";
	
	@Autowired
	private KafkaPropertiesConfig kafkaProps;
	
	@Value("${ssl.truststore.location}")
	private String TRUSTSTORE_LOCATION;

	/**
	 * Password to unlock truststore
	 */
	@Value("${ssl.truststore.password}")
	private String TRUSTSTORE_PASSWORD;

    @Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> userKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(kafkaProps.getConcurrency());
		// factory.getContainerProperties().setPollTimeout(kafkaProps.getMaxPollRecords());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProps.getBootstrapServers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProps.getGroupId());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProps.getAutoOffsetReset());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProps.getEnableAutoCommit());
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaProps.getAutoCommitIntervalMs());
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProps.getSessionTimeoutMs());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaProps.getMaxPollRecords());
		props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,
				"io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor");
		enableSSLConfig(props);
		return new DefaultKafkaConsumerFactory<>(props);
	}
	
	public void enableSSLConfig(Map<String, Object> config) {
		/* INTERCEPTOR CONFIG */
		config.put(CONFLUENT_MONITORING_INTERCEPTOR_BOOTSTRAP_SERVERS, kafkaProps.getBootstrapServers());
		config.put(CONFLUENT_MONITORING_INTERCEPTOR_SECURITY_PROTOCOL, SSL);
		config.put(CONFLUENT_MONITORING_INTERCEPTOR_SSL_TRUSTSTORE_LOCATION, TRUSTSTORE_LOCATION);
		config.put(CONFLUENT_MONITORING_INTERCEPTOR_SSL_TRUSTSTORE_PASSWORD, TRUSTSTORE_PASSWORD);
		config.put(SECURITY_PROTOCOL, SSL);
		config.put("ssl.endpoint.identification.algorithm", "");
		/* SSL CONFIG */
		config.put(SSL_TRUSTSTORE_LOCATION, TRUSTSTORE_LOCATION);
		config.put(SSL_TRUSTSTORE_PASSWORD, TRUSTSTORE_PASSWORD);
		config.put(SSL_TRUSTSTORE_TYPE, JKS);
	}

}
