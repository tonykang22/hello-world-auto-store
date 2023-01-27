package com.github.kingwaggs.productmanager.common.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResultDto implements ProductManagerResultDto {
    private final Exception exception;
    private final LocalDateTime dateTime;

    @Override
    public Object getResult() {
        return this.exception;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
}
