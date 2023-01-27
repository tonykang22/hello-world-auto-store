package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.domain.*;
import com.github.kingwaggs.productanalyzer.domain.dto.request.PopularKeywordRequestDto;
import com.github.kingwaggs.productanalyzer.domain.dto.request.SourcingContextRequestDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.*;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productanalyzer.service.productsourcing.ProductSourcingService;
import com.github.kingwaggs.productanalyzer.service.selectscore.SelectScoreService;
import com.github.kingwaggs.productanalyzer.util.FileUtil;
import com.github.kingwaggs.productanalyzer.util.PerformanceLogging;
import com.github.kingwaggs.productanalyzer.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAnalyzerService {

    private final SelectScoreService selectScoreService;
    private final ProductSourcingService productSourcingService;
    private final SourcingContext sourcingContext;
    private final SlackMessageUtil slackMessageUtil;

    public AnalyzedResultDto getSelectScores(SelectScoreType selectScoreType, LocalDate date, Integer size) {
        AnalyzedResultDto resultDto = selectScoreService.loadSelectScore(selectScoreType, date, size);
        if (resultDto instanceof ErrorResultDto) {
            slackMessageUtil.sendError("`product-analyzer` :: `GET select-scores`에 실패했습니다. \n상세 내용 :: " + resultDto.getResult());
        }
        return resultDto;
    }

    @PerformanceLogging
    public void createSelectScores(SelectScoreType selectScoreType, Integer size) {
        if (selectScoreType == null) {
            Arrays.stream(SelectScoreType.values()).forEach(eachSelectScoreType -> createSelectScoreResult(eachSelectScoreType, size));
        } else {
            createSelectScoreResult(selectScoreType, size);
        }
    }

    public AnalyzedResultDto getSourcingResults(SelectScoreType selectScoreType, LocalDate date, Integer size) {
        AnalyzedResultDto resultDto = productSourcingService.loadSourcingResult(selectScoreType, date, size);
        if (resultDto instanceof ErrorResultDto) {
            slackMessageUtil.sendError("`product-analyzer` :: `GET sourcing-results`에 실패했습니다. \n상세 내용 :: " + resultDto.getResult());
        }
        return resultDto;
    }

    @PerformanceLogging
    public void createSourcingProducts(SelectScoreType selectScoreType, LocalDate date, Integer size, List<String> asinList) {
        if (selectScoreType == null) {
            List<SelectScoreType> targetSelectScoreTypes = SelectScoreType.getScheduledSelectScoreTypes();
            targetSelectScoreTypes.forEach(type -> createAmazonSourcingProducts(type, date, size, asinList));
            return;
        }
        createAmazonSourcingProducts(selectScoreType, date, size, asinList);
    }

    private void createAmazonSourcingProducts(SelectScoreType selectScoreType, LocalDate date, Integer size, List<String> asinList) {
        switch (selectScoreType) {
            case PANDA_RANK:
            case NAVER_CATEGORY:
                createSourcingResult(selectScoreType, date, size);
                break;
            case CUSTOM:
                createCustomAmazonSourcingResult(asinList);
                return;
            case POPULAR_KEYWORD:
                createPopularKeywordSourcingResult();
                break;
            default:
                throw new IllegalStateException("Unreachable statement.");
        }
    }

    public AnalyzedResultDto getSourcingContext() {
        return new SourcingContextResultDto(this.sourcingContext, LocalDateTime.now());
    }

    public AnalyzedResultDto getSelectScoreContext(SelectScoreType selectScoreType) {
        SelectScoreContext selectScoreContext = sourcingContext.getSelectScoreContext(selectScoreType);
        return new SelectScoreContextResultDto(selectScoreContext, LocalDateTime.now());
    }

    public AnalyzedResultDto updateSourcingContext(SourcingContextRequestDto requestDto) {
        LocalDateTime requestedTime = LocalDateTime.now();
        try {
            this.sourcingContext.update(requestDto);
            return new SourcingContextResultDto(this.sourcingContext, requestedTime);
        } catch (IllegalArgumentException illegalArgumentException) {
            slackMessageUtil.sendError("`product-analyzer` :: `POST sourcing-context`에 실패했습니다. \n상세 내용 :: " + illegalArgumentException);
            return new ErrorResultDto(illegalArgumentException, requestedTime);
        }
    }

    public AnalyzedResultDto updateSelectScoreContext(SelectScoreType selectScoreType, Integer pointer) {
        SelectScoreContext selectScoreContext = this.sourcingContext.getSelectScoreContext(selectScoreType);
        selectScoreContext.updatePointer(pointer);
        return new SelectScoreContextResultDto(selectScoreContext, LocalDateTime.now());
    }

    public AnalyzedResultDto getForbiddenWords() {
        return productSourcingService.getForbiddenWords();
    }

    public AnalyzedResultDto addForbiddenWords(List<String> targetWords) {
        return productSourcingService.addForbiddenWords(targetWords);
    }

    public void deleteForbiddenWords(List<String> targetWords) {
        productSourcingService.deleteForbiddenWord(targetWords);
    }

    public AnalyzedResultDto getPopularKeywords() {
        return productSourcingService.getPopularKeywords();
    }

    public AnalyzedResultDto addPopularKeywords(PopularKeywordRequestDto requestDto) {
        return productSourcingService.addPopularKeywords(requestDto);
    }

    public void deletePopularKeywords(List<String> targetWords) {
        productSourcingService.deletePopularKeywords(targetWords);
    }

    private void createSelectScoreResult(SelectScoreType selectScoreType, Integer size) {
        AnalyzedResultDto resultDto = selectScoreService.createSelectScore(selectScoreType, size);
        if (resultDto instanceof ErrorResultDto) {
            log.error("AnalyzedResult is Error. Skip creating file. (AnalyzedResultDto : {})", resultDto);
            slackMessageUtil.sendError("`product-analyzer` :: `create select-score`에 실패했습니다. \n상세 내용 :: " + resultDto.getResult());
            return;
        }
        createResultFile(FileType.SELECT_SCORES, selectScoreType, resultDto);
        initSelectScoreContext(selectScoreType, (SelectScoreResultDto) resultDto);
        String slackMessage = String.format("`product-analyzer` :: `파일 생성`에 성공했습니다. " +
                        "fileType : %s, selectScoreType : %s", FileType.SELECT_SCORES, selectScoreType);
        slackMessageUtil.sendSuccess(slackMessage);
    }

    private void initSelectScoreContext(SelectScoreType selectScoreType, SelectScoreResultDto resultDto) {
        SelectScoreContext selectScoreContext = sourcingContext.getSelectScoreContext(selectScoreType);
        selectScoreContext.initContext((List<SelectScore>) resultDto.getResult(), resultDto.getTemporal());
        log.info("SelectScoreContext[{}] was initialized after new SelectScoreList were made.", selectScoreType);
    }

    private void createCustomAmazonSourcingResult(List<String> asinList) {
        AnalyzedResultDto resultDto = productSourcingService.createCustomSourcingResults(asinList);
        if (resultDto instanceof ErrorResultDto) {
            slackMessageUtil.sendError("`product-analyzer` :: `create sourcing results`에 실패했습니다. \n상세 내용 :: " + resultDto.getResult());
            log.error("AnalyzedResult is Error. Skip creating file. (AnalyzedResultDto : {})", resultDto);
            return;
        }
        createResultFile(FileType.SOURCING_RESULTS, SelectScoreType.CUSTOM, resultDto);
        String slackMessage = String.format("`product-analyzer` :: `파일 생성`에 성공했습니다. " +
                "fileType : %s, selectScoreType : %s", FileType.SOURCING_RESULTS, SelectScoreType.CUSTOM);
        slackMessageUtil.sendSuccess(slackMessage);
    }

    private void createPopularKeywordSourcingResult() {
        AnalyzedResultDto resultDto = productSourcingService.createPopularKeywordSourcingResults();
        if (resultDto instanceof ErrorResultDto) {
            slackMessageUtil.sendError("`product-analyzer` :: `create sourcing results`에 실패했습니다. \n상세 내용 :: " + resultDto.getResult());
            log.error("AnalyzedResult is Error. Skip creating file. (AnalyzedResultDto : {})", resultDto);
            return;
        }
        SourcingResultDto.SourcingDto sourcingDto = (SourcingResultDto.SourcingDto) resultDto.getResult();
        AnalyzedResultDto productDto =
                new SourcingProductDto(sourcingDto.getSourcingProductList(), (LocalDate) resultDto.getTemporal());

        createResultFile(FileType.SOURCING_RESULTS, SelectScoreType.POPULAR_KEYWORD, productDto);

        String searchWord = String.join(", ", sourcingDto.getSearchWordList());
        String slackMessage = String.format("`product-analyzer` :: `파일 생성`에 성공했습니다. " +
                "fileType : %s, selectScoreType : %s, searchWord : %s",
                FileType.SOURCING_RESULTS, SelectScoreType.POPULAR_KEYWORD, searchWord);
        slackMessageUtil.sendSuccess(slackMessage);
    }

    private void createSourcingResult(SelectScoreType selectScoreType, LocalDate date, Integer size) {
        SelectScoreContext selectScoreContext = sourcingContext.getSelectScoreContext(selectScoreType);
        configureContext(selectScoreContext, date);
        int sourcingCount = getSourcingCount(selectScoreContext, size);
        AnalyzedResultDto resultDto = productSourcingService.createSourcingResults(selectScoreContext, sourcingCount);
        if (resultDto instanceof ErrorResultDto) {
            slackMessageUtil.sendError("`product-analyzer` :: `create sourcing results`에 실패했습니다. \n상세 내용 :: " + resultDto.getResult());
            log.error("AnalyzedResult is Error. Skip creating file. (AnalyzedResultDto : {})", resultDto);
            return;
        }
        SourcingResultDto.SourcingDto sourcingDto = (SourcingResultDto.SourcingDto) resultDto.getResult();
        LocalDate resultCreatedAt = (LocalDate) resultDto.getTemporal();
        List<AmazonSourcingProduct> sourcingProductList = sourcingDto.getSourcingProductList();
        AnalyzedResultDto productDto = new SourcingProductDto(sourcingProductList, resultCreatedAt);

        createResultFile(FileType.SOURCING_RESULTS, selectScoreContext.getSelectScoreType(), productDto);

        String searchWord = String.join(", ", sourcingDto.getSearchWordList());
        String slackMessage = String.format("`product-analyzer` :: `파일 생성`에 성공했습니다. " +
                        "fileType : %s, selectScoreType : %s, searchWord : %s",
                FileType.SOURCING_RESULTS, selectScoreContext.getSelectScoreType(), searchWord);
        slackMessageUtil.sendSuccess(slackMessage);
    }

    private void configureContext(SelectScoreContext selectScoreContext, LocalDate date) {
        SelectScoreType selectScoreType = selectScoreContext.getSelectScoreType();
        LocalDate targetVersion = getTargetVersion(selectScoreType, date);
        if (targetVersion == null || !selectScoreContext.isNewVersion(targetVersion)) {
            log.info("New version of select score file does not exist. Skip initialize SelectScoreContext. (selectScoreType : {})", selectScoreType);
            return;
        }
        AnalyzedResultDto selectScoreResultDto = selectScoreService.loadSelectScore(selectScoreType, targetVersion, null);
        if (selectScoreResultDto instanceof ErrorResultDto) {
            log.error("SelectScoreDto is null. Skip initialize SelectScoreContext. (selectScoreType : {}, targetVersion : {})", selectScoreType, targetVersion);
            return;
        }
        List<SelectScore> selectScoreList = (List<SelectScore>) selectScoreResultDto.getResult();
        LocalDate createdAt = LocalDate.from(selectScoreResultDto.getTemporal());
        selectScoreContext.loadContext(selectScoreList, createdAt);
        log.info("selectScoreContext initialized with new selectScoreList. (selectScoreType : {}, selectScoreList createdAt : {})", selectScoreType, createdAt);
    }

    private LocalDate getTargetVersion(SelectScoreType selectScoreType, LocalDate date) {
        return date == null
                ? selectScoreService.getLatestVersion(selectScoreType)
                : date;
    }

    private int getSourcingCount(SelectScoreContext selectScoreContext, Integer size) {
        int sourcingTotalCount = size == null
                ? sourcingContext.getSourcingTotalCount()
                : size;
        double ratio = selectScoreContext.getSourcingRatio();
        return (int) (sourcingTotalCount * ratio);
    }

    private void createResultFile(FileType fileType, SelectScoreType selectScoreType, AnalyzedResultDto analyzedResultDto) {
        Object result = analyzedResultDto.getResult();
        if (result == null) {
            log.error("Result in AnalyzedResultDto is null. " +
                    "Skip writing file. (selectScoreType : {}).", selectScoreType.getType());
            return;
        }
        try {
            LocalDate createdAt = LocalDate.from(analyzedResultDto.getTemporal());
            FileUtil.writeFile(fileType, selectScoreType, createdAt, result);
        } catch (IOException ioException) {
            log.error("Exception occurred while writeFile(selectScoreType : {}).", selectScoreType.getType());
            return;
        }
        log.info("createResultFile finished successfully. (fileType : {}, selectScoreType : {})",
                fileType.getType(), selectScoreType.getType());
    }

}
