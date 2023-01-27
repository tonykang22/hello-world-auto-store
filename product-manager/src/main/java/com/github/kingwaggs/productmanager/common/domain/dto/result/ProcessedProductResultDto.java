package com.github.kingwaggs.productmanager.common.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@AllArgsConstructor
public class ProcessedProductResultDto implements ProductManagerResultDto {
    private final List<String> productIdList;
    private final LocalDateTime dateTime;

    @Override
    public Object getResult() {
        return this.productIdList;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

}
