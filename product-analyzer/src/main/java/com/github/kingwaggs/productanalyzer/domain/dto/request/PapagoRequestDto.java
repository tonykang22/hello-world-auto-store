package com.github.kingwaggs.productanalyzer.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PapagoRequestDto {
    private final String source;
    private final String target;
    private final String text;
}
