package com.example.ml.core.pipeline;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.model.MlJobStatus;
import com.example.ml.core.model.MlResult;
import com.example.ml.core.port.outgoing.MessageBrokerPort;
import com.example.ml.core.port.outgoing.MlJobDatabasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(prefix = "ml.processing", name = "mock-enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class MockMlProcessingPipeline implements MlJobProcessingPipeline {
    private final MlJobDatabasePort databasePort;
    private final MessageBrokerPort messageBrokerPort;

    @Override
    @Async
    public void processJobAsync(MlJob job) {
        try {
            job.changeStatus(MlJobStatus.TRANSCRIBING);
            databasePort.save(job);

            MlResult result = generateMockResult();
            job.changeResult(new MlResult(result.transcript(), null));
            Thread.sleep(3000);
            job.changeStatus(MlJobStatus.ANALYSING);
            databasePort.save(job);

            Thread.sleep(3000);
            job.complete(result);
            databasePort.save(job);
        } catch (Exception e) {
            log.error("Mock processing failed for job '{}'", job.getJobId(), e);
            job.changeStatus(MlJobStatus.FAILED);
            databasePort.save(job);
        }

        messageBrokerPort.publish(job);
    }

    private MlResult generateMockResult() {
        return new MlResult(
                new MlResult.Transcript(List.of(
                        new MlResult.Segment(1, "SPEAKER_01", 0.5, 5.8,
                                "Добрый день, это тестовая расшифровка от заглушки."),
                        new MlResult.Segment(2,"SPEAKER_02", 6.0, 10.6,
                                "И вам добрый день, расшифровка тестовая")
                )),
                new MlResult.Analysis(
                        "На встрече обсуждалось создание API-заглушки.",
                        List.of("API", "Spring Boot"),
                        List.of("Заглушка готова, можно интегрироваться"),
                        List.of("Начать разработку личного кабинета"),
                        List.of(new MlResult.TaskItem("Настроить БД", "бекендер2"))
                )
        );
    }
}
