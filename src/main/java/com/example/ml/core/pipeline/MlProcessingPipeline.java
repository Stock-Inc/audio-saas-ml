package com.example.ml.core.pipeline;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.model.MlJobStatus;
import com.example.ml.core.model.MlResult;
import com.example.ml.core.port.outgoing.MlJobDatabasePort;
import com.example.ml.core.port.outgoing.WebhookPort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MlProcessingPipeline {

    private final MlJobDatabasePort databasePort;
    private final WebhookPort webhookPort;

    @Async
    public void processJobAsync(MlJob job) {
        try {

            job.changeStatus(MlJobStatus.PROCESSING);
            databasePort.save(job);

            Thread.sleep(10000);

            MlResult mockResult = generateMockResult();

            job.complete(mockResult);
            databasePort.save(job);

            webhookPort.sendCallback(
                    job.getCallbackUrl(),
                    job.getJobId(),
                    job.getRecordId(),
                    job.getStatus().name()
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private MlResult generateMockResult() {
        return new MlResult(
                new MlResult.Transcript(List.of(
                        new MlResult.Segment(1, "SPEAKER_01", 0.5, 5.8, "Добрый день, это тестовая расшифровка от заглушки.")
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