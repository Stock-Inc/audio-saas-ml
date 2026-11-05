package com.example.ml.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WhisperResponse(
        List<WhisperSegmentDto> segments
){
}
