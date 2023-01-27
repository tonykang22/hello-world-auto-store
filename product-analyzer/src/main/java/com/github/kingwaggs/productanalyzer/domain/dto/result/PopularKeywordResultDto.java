package com.github.kingwaggs.productanalyzer.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.domain.entity.PopularKeyword;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@AllArgsConstructor
public class PopularKeywordResultDto implements AnalyzedResultDto {

    private final List<PopularKeyword> popularKeywordList;
    private final LocalDate date;

    @Override
    public List<PopularKeyword> getResult() {
        return this.popularKeywordList;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Override
    public LocalDate getTemporal() {
        return this.date;
    }
}
