package com.example.ml.infrastructure.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${ml.kafka.topic.job-results}")
    private String topicName;

    @Value("${ml.kafka.topic.partitions}")
    private int partitions;

    @Value("${ml.kafka.topic.replicas}")
    private int replicas;

    @Bean
    public NewTopic jobResultsTopic() {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
