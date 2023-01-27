package com.github.kingwaggs.productmanager.common.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@ToString
@AllArgsConstructor
public class SyncedCoupangResultDto implements ProductManagerResultDto {

    private final Set<String> changedProductIdSet;
    private final LocalDateTime syncedAt;

    @Override
    public Set<String> getResult() {
        return this.changedProductIdSet;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public LocalDateTime getDateTime() {
        return this.syncedAt;
    }

}
