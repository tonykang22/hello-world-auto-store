package com.github.kingwaggs.productanalyzerv2.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class ErrorResult implements AnalyzedResult {

    public ErrorResult(Exception exception, LocalDateTime createdAt) {
        this.error = new Error(exception);
        this.createdAt = createdAt;
    }

    private final Error error;
    private final LocalDateTime createdAt;

    @Override
    public Error getResult() {
        return this.error;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public record Error(Exception exception) implements Result {
    }

}
