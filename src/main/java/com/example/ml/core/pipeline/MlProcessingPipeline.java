package com.example.ml.core.pipeline;

import com.example.ml.core.model.MlJob;
import com.example.ml.core.model.MlJobStatus;
import com.example.ml.core.model.MlResult;
import com.example.ml.core.port.outgoing.MlJobDatabasePort;
import com.example.ml.core.port.outgoing.SttClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MlProcessingPipeline {

    private final MlJobDatabasePort databasePort;
    private final SttClientPort sttClientPort;

    @Async
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