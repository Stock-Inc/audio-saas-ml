package com.example.ml.infrastructure.kafka;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.port.outgoing.MessageBrokerPort;
import com.example.ml.infrastructure.kafka.dto.MlJobResultEvent;
import com.example.ml.infrastructure.kafka.mapper.MlJobResultEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class KafkaEventProducerAdapter implements MessageBrokerPort {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final MlJobResultEventMapper mapper;
    private final String resultTopic;

    public KafkaEventProducerAdapter(KafkaTemplate<String, Object> kafkaTemplate,
                                     MlJobResultEventMapper mapper,
                                     @Value("${ml.kafka.topic.job-results}") String resultTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
        this.resultTopic = resultTopic;
    }

    @Override
    public void publish(MlJob mlJob) {
        MlJobResultEvent event = mapper.toEvent(mlJob);
        try {
            kafkaTemplate.send(resultTopic, mlJob.getJobId().toString(), event).get();
            log.info("Event '{}' published in Kafka", mlJob.getJobId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while publishing job " + mlJob.getJobId(), e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Failed to publish job " + mlJob.getJobId(), e.getCause());
        }
    }
}
