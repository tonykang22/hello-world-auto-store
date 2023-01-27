package com.github.kingwaggs.csmanager.domain.result;

import java.time.LocalDateTime;

public interface CsManagerResultDto {
    Object getResult();
    LocalDateTime getDateTime();
}
