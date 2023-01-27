package com.github.kingwaggs.productmanager.common.domain.dto.result;

import java.time.LocalDateTime;

public interface ProductManagerResultDto {
    Object getResult();
    LocalDateTime getDateTime();
}
