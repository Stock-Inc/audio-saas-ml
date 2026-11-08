package com.example.ml.core.pipeline;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.model.MlJobStatus;
import com.example.ml.core.model.MlResult;
import com.example.ml.core.port.outgoing.MessageBrokerPort;
import com.example.ml.core.port.outgoing.MlJobDatabasePort;
import com.example.ml.core.port.outgoing.SttClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "ml.processing", name = "mock-enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class MlProcessingPipeline implements JobProcessingPipeline {

    private final MlJobDatabasePort databasePort;
    private final SttClientPort sttClientPort;
    private final MessageBrokerPort messageBrokerPort;

    @Async
    @Override
    public void processJobAsync(MlJob job) {
        try {
            job.changeStatus(MlJobStatus.TRANSCRIBING);
            databasePort.save(job);

            MlResult.Transcript transcript = sttClientPort.transcribe(job.getFileUrl(), true);
            MlResult halfResult = new MlResult(transcript, null);
            job.changeResult(halfResult);
            job.changeStatus(MlJobStatus.ANALYSING);
            databasePort.save(job);

            job.complete(halfResult);
            databasePort.save(job);

        } catch (Exception e) {
            log.error("Processing failed for job '{}'", job.getJobId(), e);
            job.changeStatus(MlJobStatus.FAILED);
            databasePort.save(job);
        }

        messageBrokerPort.publish(job);
    }
}
