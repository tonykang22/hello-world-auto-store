package com.github.kingwaggs.productanalyzer.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.domain.ForbiddenWord;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@AllArgsConstructor
public class ForbiddenWordResultDto implements AnalyzedResultDto {

    private final List<ForbiddenWord> forbiddenWordList;
    private final LocalDate date;

    @Override
    public List<ForbiddenWord> getResult() {
        return this.forbiddenWordList;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Override
    public LocalDate getTemporal() {
        return this.date;
    }
}
