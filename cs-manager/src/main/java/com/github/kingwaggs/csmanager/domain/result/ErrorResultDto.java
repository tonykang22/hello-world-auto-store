package com.github.kingwaggs.csmanager.domain.result;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
public class ErrorResultDto implements CsManagerResultDto {

    private Exception exception;
    private LocalDateTime dateTime;

    @Override
    public Exception getResult() {
        return this.exception;
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
}
