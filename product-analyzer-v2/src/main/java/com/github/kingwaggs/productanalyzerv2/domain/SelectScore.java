package com.github.kingwaggs.productanalyzerv2.domain;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SelectScore {

    private int rank;
    private String searchWord;
    private Double score;

}
