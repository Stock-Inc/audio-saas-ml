package com.example.ml.infrastructure.data.entity;

import com.example.ml.core.model.MlJobStatus;
import com.example.ml.core.model.MlResult;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "ml_jobs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MlJobEntity {
    @Id
    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "record_id", nullable = false)
    private UUID recordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MlJobStatus status;

    @Column(name = "file_url")
    private String fileUrl;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "opt_diarization")
    private boolean diarization;
    @Column(name = "opt_summary")
    private boolean summary;
    @Column(name = "opt_tasks")
    private boolean tasks;

    @Column(name = "callback_url", nullable = false)
    private String callbackUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ml_result", columnDefinition = "jsonb")
    private MlResult mlResult;
}