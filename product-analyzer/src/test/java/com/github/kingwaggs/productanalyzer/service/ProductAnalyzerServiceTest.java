package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.domain.*;
import com.github.kingwaggs.productanalyzer.domain.dto.request.SourcingContextRequestDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.AnalyzedResultDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.SelectScoreResultDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.SourcingResultDto;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productanalyzer.service.productsourcing.ProductSourcingService;
import com.github.kingwaggs.productanalyzer.service.selectscore.SelectScoreService;
import com.github.kingwaggs.productanalyzer.util.FileUtil;
import com.github.kingwaggs.productanalyzer.util.PathFinder;
import com.github.kingwaggs.productanalyzer.util.SlackMessageUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductAnalyzerServiceTest {

    private final SelectScoreService selectScoreService = mock(SelectScoreService.class);
    private final ProductSourcingService productSourcingService = mock(ProductSourcingService.class);
    private final SourcingContext sourcingContext = mock(SourcingContext.class);
    private final SlackMessageUtil slackMessageUtil = mock(SlackMessageUtil.class);
    private final ProductAnalyzerService productAnalyzerService = new ProductAnalyzerService(selectScoreService, productSourcingService, sourcingContext, slackMessageUtil);

    @BeforeAll
    static void init() {
        PathFinder pathFinder = new PathFinder();
        pathFinder.initRootPath("..");
    }

    @Test
    void getSelectScores() {
        // Arrange
        AnalyzedResultDto selectScoreResultDto = mock(SelectScoreResultDto.class);
        when(selectScoreService.loadSelectScore(any(), any(), any())).thenReturn(selectScoreResultDto);

        // Act
        productAnalyzerService.getSelectScores(SelectScoreType.NAVER_CATEGORY, null, null);

        // Assert
        verify(selectScoreService).loadSelectScore(any(), any(), any());
    }

    @Test
    void createSelectScores() {
        // Arrange
        LocalDate targetDate = LocalDate.now();
        AnalyzedResultDto selectScoreResultDto = mock(SelectScoreResultDto.class);
        when(selectScoreResultDto.getResult()).thenReturn("test");
        when(selectScoreResultDto.getTemporal()).thenReturn(targetDate);
        when(selectScoreService.createSelectScore(any(), any())).thenReturn(selectScoreResultDto);

        // Act
        productAnalyzerService.createSelectScores(SelectScoreType.NAVER_CATEGORY, null);

        // Assert
        verify(selectScoreService).createSelectScore(any(), any());
        assertTrue(FileUtil.isExist(FileType.SELECT_SCORES, SelectScoreType.NAVER_CATEGORY, targetDate));
    }

    @Test
    void getSourcingResults() {
        // Arrange
        AnalyzedResultDto sourcingResultDto = mock(SourcingResultDto.class);
        when(productSourcingService.loadSourcingResult(any(), any(), any())).thenReturn(sourcingResultDto);

        // Act
        productAnalyzerService.getSourcingResults(SelectScoreType.NAVER_CATEGORY, null, null);

        // Assert
        verify(productSourcingService).loadSourcingResult(any(), any(), any());
    }

    @Test
    void createAmazonSourcingProducts() throws IOException {
        // Arrange
        SelectScoreType type = SelectScoreType.NAVER_CATEGORY;
        SelectScoreContext selectScoreContext = SelectScoreContext.createInitialContext(type);
        LocalDate testDate = LocalDate.now();

        SelectScore testSelectScore = mock(SelectScore.class);
        List<SelectScore> testSelectScoreList = Collections.singletonList(testSelectScore);
        AnalyzedResultDto selectScoreResultDto = new SelectScoreResultDto(testSelectScoreList, testDate);

        AmazonSourcingProduct testProduct = mock(AmazonSourcingProduct.class);
        List<AmazonSourcingProduct> testProductList = Collections.singletonList(testProduct);
        AnalyzedResultDto sourcingResultDto = new SourcingResultDto(testProductList, Collections.emptyList(),testDate);

        when(sourcingContext.getSelectScoreContext(any())).thenReturn(selectScoreContext);
        when(selectScoreService.getLatestVersion(any())).thenReturn(testDate);
        when(selectScoreService.loadSelectScore(any(), any(), any())).thenReturn(selectScoreResultDto);
        when(sourcingContext.getSourcingTotalCount()).thenReturn(100);
        when(productSourcingService.createSourcingResults(any(SelectScoreContext.class), anyInt())).thenReturn(sourcingResultDto);

        // Act
        productAnalyzerService.createSourcingProducts(type, null, null, null);

        // Assert
        verify(sourcingContext).getSelectScoreContext(any());
        verify(selectScoreService).getLatestVersion(any());
        verify(selectScoreService).loadSelectScore(any(), any(), any());
        verify(sourcingContext).getSourcingTotalCount();
        verify(productSourcingService).createSourcingResults(any(SelectScoreContext.class), anyInt());
        assertEquals(testDate, selectScoreContext.getDate());
        assertEquals(testSelectScoreList, selectScoreContext.getSelectScoreList());
        assertTrue(FileUtil.isExist(FileType.SOURCING_RESULTS, type, testDate));

        String writtenJson = FileUtil.readFile(FileType.SOURCING_RESULTS, type, testDate);
        Object writtenObject = FileUtil.readValue(FileType.SOURCING_RESULTS, writtenJson);
        List<AmazonSourcingProduct> actual = (List<AmazonSourcingProduct>) writtenObject;
        assertEquals(1, actual.size());
    }

    @Test
    void getSourcingContext() {
        // Arrange

        // Act
        AnalyzedResultDto actual = productAnalyzerService.getSourcingContext();

        // Assert
        assertNotNull(actual);
    }

    @Test
    void updateSourcingContext() {
        // Arrange
        SourcingContextRequestDto sourcingContextRequestDto = new SourcingContextRequestDto(200, 7L, new ArrayList<>());
        doNothing().when(sourcingContext).update(any(SourcingContextRequestDto.class));

        // Act
        AnalyzedResultDto actual = productAnalyzerService.updateSourcingContext(sourcingContextRequestDto);

        // Assert
        assertNotNull(actual);
        verify(sourcingContext).update(any(SourcingContextRequestDto.class));
    }
}