package com.github.kingwaggs.productmanager.common.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productmanager.common.domain.dto.SyncResult;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@AllArgsConstructor
public class SyncedAmazonResultDto implements ProductManagerResultDto {

    private final List<SyncResult> syncResultList;
    private final LocalDateTime syncedAt;

    @Override
    public List<SyncResult> getResult() {
        return this.syncResultList;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public LocalDateTime getDateTime() {
        return this.syncedAt;
    }

}
