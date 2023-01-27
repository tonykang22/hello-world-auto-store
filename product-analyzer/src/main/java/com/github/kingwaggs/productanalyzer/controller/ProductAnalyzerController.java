package com.github.kingwaggs.productanalyzer.controller;

import com.github.kingwaggs.productanalyzer.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzer.domain.dto.request.PopularKeywordRequestDto;
import com.github.kingwaggs.productanalyzer.domain.dto.request.SourcingContextRequestDto;
import com.github.kingwaggs.productanalyzer.domain.dto.response.CommonResponse;
import com.github.kingwaggs.productanalyzer.domain.dto.result.AnalyzedResultDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.ErrorResultDto;
import com.github.kingwaggs.productanalyzer.service.ProductAnalyzerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/product-analyzer")
@RequiredArgsConstructor
public class ProductAnalyzerController {

    private final ProductAnalyzerService productAnalyzerService;

    @GetMapping("/select-scores")
    public ResponseEntity<CommonResponse> getSelectScores(
            @RequestParam(name = "selectScoreType") SelectScoreType selectScoreType,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "size", required = false) Integer size) {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.getSelectScores(selectScoreType, date, size);
        if (analyzedResultDto instanceof ErrorResultDto) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, analyzedResultDto);
            return ResponseEntity.internalServerError().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/select-scores")
    public ResponseEntity<CommonResponse> createSelectScores(
            @RequestParam(name = "selectScoreType", required = false) SelectScoreType selectScoreType,
            @RequestParam(name = "size", required = false) Integer size) {
        new Thread(() -> productAnalyzerService.createSelectScores(selectScoreType, size), "select-scores-thread").start();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, "Request sent successfully.");
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/sourcing-results")
    public ResponseEntity<CommonResponse> getSourcingResults(
            @RequestParam(name = "selectScoreType") SelectScoreType selectScoreType,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "size", required = false) Integer size) {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.getSourcingResults(selectScoreType, date, size);
        if (analyzedResultDto instanceof ErrorResultDto) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, analyzedResultDto);
            return ResponseEntity.internalServerError().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/sourcing-results")
    public ResponseEntity<CommonResponse> createSourcingResults(
            @RequestParam(name = "selectScoreType", required = false) SelectScoreType selectScoreType,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "asin", required = false) List<String> asinList) {
        new Thread(() -> productAnalyzerService.createSourcingProducts(selectScoreType, date, size, asinList),
                "sourcing-results-thread").start();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, "Request sent successfully.");
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/sourcing-context")
    public ResponseEntity<CommonResponse> getSourcingContext() {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.getSourcingContext();
        if (analyzedResultDto instanceof ErrorResultDto) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, analyzedResultDto);
            return ResponseEntity.internalServerError().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/sourcing-context")
    public ResponseEntity<CommonResponse> updateSourcingContext(@RequestBody SourcingContextRequestDto requestDto) {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.updateSourcingContext(requestDto);
        if (analyzedResultDto instanceof ErrorResultDto) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, analyzedResultDto);
            return ResponseEntity.internalServerError().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }
    // TODO : file delete
    // TODO : select-score indicator context

    @GetMapping("/select-score-context")
    public ResponseEntity<CommonResponse> getSelectScoreContext(@RequestParam SelectScoreType selectScoreType) {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.getSelectScoreContext(selectScoreType);
        if (analyzedResultDto instanceof ErrorResultDto) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, analyzedResultDto);
            return ResponseEntity.internalServerError().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/select-score-context")
    public ResponseEntity<CommonResponse> updateSelectScoreContext(@RequestParam SelectScoreType selectScoreType,
                                                                   @RequestParam Integer pointer) {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.updateSelectScoreContext(selectScoreType, pointer);
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/forbidden-words")
    public ResponseEntity<CommonResponse> getForbiddenWords() {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.getForbiddenWords();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/forbidden-words")
    public ResponseEntity<CommonResponse> addForbiddenWords(@RequestParam List<String> targetWords) {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.addForbiddenWords(targetWords);
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/forbidden-words")
    public ResponseEntity<CommonResponse> deleteForbiddenWords(@RequestParam List<String> targetWords) {
        productAnalyzerService.deleteForbiddenWords(targetWords);
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, "Request sent successfully.");
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/popular-keywords")
    public ResponseEntity<CommonResponse> getPopularKeywords() {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.getPopularKeywords();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping("/popular-keywords")
    public ResponseEntity<CommonResponse> addPopularKeywords(@RequestBody PopularKeywordRequestDto requestDto) {
        AnalyzedResultDto analyzedResultDto = productAnalyzerService.addPopularKeywords(requestDto);
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, analyzedResultDto);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/popular-keywords")
    public ResponseEntity<CommonResponse> deletePopularKeywords(@RequestParam List<String> targetWords) {
        productAnalyzerService.deletePopularKeywords(targetWords);
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, "Request sent successfully.");
        return ResponseEntity.ok(commonResponse);
    }

}
