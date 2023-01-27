package com.github.kingwaggs.productanalyzer.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PapagoWebPageRequestDto {
    String html;
    String source;
    String target;
}
