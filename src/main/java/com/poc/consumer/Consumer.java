package com.poc.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
	
	@KafkaListener(
			topics = "${topicname}",
			groupId = "${kafka.group-id}", 
			containerFactory = "userKafkaListenerFactory")
	public void consumeJson(ConsumerRecord<String,String> record) {
		System.out.println(
				"Consumed JSON Message: Key-" + record.key()
				+" Value: "+record.value().toString()
				);
	}
}
