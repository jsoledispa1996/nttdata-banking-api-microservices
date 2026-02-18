package com.nttdata.banking.customer.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class KafkaConfig {
    
    @Value("${kafka.topics.customer-events}")
    private String customerEventsTopic;
    
    /**
     * Create customer-events topic.
     * 
     * Configuration:
     * - 3 partitions for parallelism
     * - Replication factor 1 
     */
    @Bean
    public NewTopic customerEventsTopic() {
        return TopicBuilder.name(customerEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
