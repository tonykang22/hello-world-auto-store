package com.github.kingwaggs.productmanager.common.domain.dto;

import lombok.Data;

@Data
public class SyncResult {

    private final String productId;
    private final SyncEntry before;
    private final SyncEntry after;

    @Data
    public static class SyncEntry {

        private final Double sourcePrice;
        private final Integer stockCount;

    }

}
