/**
 * 
 */
package com.poc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ckulkar
 *
 */
@Getter
@Setter
@Component
@ConfigurationProperties("kafka")
public class KafkaPropertiesConfig {
	private String bootstrapServers;
	private String groupId;
	private String autoOffsetReset;
	private int sessionTimeoutMs;
	private Boolean enableAutoCommit;
	private int autoCommitIntervalMs;
	private int maxPollRecords;
	private int concurrency;
	private int pollTimeout;
	private String acks;
	private int batchSize;
	private long bufferMemory;
	private int lingerMs;
	private int retries;
}

