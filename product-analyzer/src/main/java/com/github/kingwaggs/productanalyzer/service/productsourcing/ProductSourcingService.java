package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.github.kingwaggs.productanalyzer.domain.*;
import com.github.kingwaggs.productanalyzer.domain.dto.request.PopularKeywordRequestDto;
import com.github.kingwaggs.productanalyzer.domain.dto.response.AmazonProductResponseDto;
import com.github.kingwaggs.productanalyzer.domain.dto.result.*;
import com.github.kingwaggs.productanalyzer.domain.entity.PopularKeyword;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productanalyzer.domain.product.SourcingItem;
import com.github.kingwaggs.productanalyzer.domain.product.SourcingProduct;
import com.github.kingwaggs.productanalyzer.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzer.exception.RainforestException;
import com.github.kingwaggs.productanalyzer.service.ForbiddenWordService;
import com.github.kingwaggs.productanalyzer.service.HelloWorldAutoStoreProductService;
import com.github.kingwaggs.productanalyzer.service.PopularKeywordService;
import com.github.kingwaggs.productanalyzer.util.FileUtil;
import com.github.kingwaggs.productanalyzer.util.PerformanceLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSourcingService {

    public static final double MINIMUM_PRICE_POLICY = 40;

    private static final int POPULAR_MAX_PAGE = 5;
    private static final int DEFAULT_MAX_PAGE = 1;
    private static final String TRANSLATE_FROM = "ko";
    private static final String TRANSLATE_TO = "en";

    private final RainforestService rainforestService;
    private final AmazonSourcingProductFactory amazonSourcingProductFactory;
    private final PapagoTextTranslator papagoTextTranslator;
    private final HelloWorldAutoStoreProductService hwasProductService;
    private final ForbiddenWordService forbiddenWordService;
    private final PopularKeywordService popularKeywordService;

    private static final AtomicBoolean running = new AtomicBoolean(false);
    private static final Set<String> forbiddenWords = new HashSet<>();
    private volatile LocalDateTime startedAt = LocalDateTime.MIN;

    public AnalyzedResultDto loadSourcingResult(SelectScoreType selectScoreType, LocalDate date, Integer size) {
        try {
            if (date == null) {
                date = FileUtil.findLatestDate(FileType.SOURCING_RESULTS, selectScoreType);
            }
            String decodedJson = FileUtil.readFile(FileType.SOURCING_RESULTS, selectScoreType, date);
            java.lang.Object mappedJson = FileUtil.readValue(FileType.SOURCING_RESULTS, decodedJson);
            List<AmazonSourcingProduct> sourcingProductList = (List<AmazonSourcingProduct>) mappedJson;
            sourcingProductList = size == null ? sourcingProductList : sourcingProductList.subList(0, size);
            return new SourcingResultDto(sourcingProductList, Collections.emptyList(), date);
        } catch (Exception exception) {
            log.error("Unhandled exception occurred while loadSourcingResult.", exception);
            return new ErrorResultDto(exception, date);
        }
    }

    @PerformanceLogging
    public AnalyzedResultDto createSourcingResults(SelectScoreContext context, int sourcingCount) {
        SelectScoreType selectScoreType = context.getSelectScoreType();
        return createSourcingProducts(selectScoreType, context, sourcingCount);
    }

    @PerformanceLogging
    public AnalyzedResultDto createCustomSourcingResults(List<String> asinList) {
        SelectScoreType custom = SelectScoreType.CUSTOM;
        if (isRunning()) {
            String message = getStatusMessage(custom);
            return new ErrorResultDto(new AlreadyWorkingException(message), LocalDate.now());
        }
        lockIndicators();
        updateForbiddenWordSet();
        List<AmazonProductResponseDto> amazonProductList = asinList.stream()
                .filter(a -> !hwasProductService.isAlreadyRegistered(Vendor.AMAZON_US, a))
                .map(rainforestService::fetchProduct)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<AmazonSourcingProduct> sourcingProductList = createSourcingResults(amazonProductList);
        unlockIndicators();
        return new SourcingProductDto(sourcingProductList, LocalDate.now());
    }

    @Transactional
    @PerformanceLogging
    public AnalyzedResultDto createPopularKeywordSourcingResults() {
        SelectScoreType popularKeyword = SelectScoreType.POPULAR_KEYWORD;
        if (isRunning()) {
            String message = getStatusMessage(popularKeyword);
            return new ErrorResultDto(new AlreadyWorkingException(message), LocalDate.now());
        }
        lockIndicators();
        updateForbiddenWordSet();

        List<PopularKeyword> targetPopularKeywords = popularKeywordService.getTargetPopularKeywords();
        List<String> keywordList = targetPopularKeywords.stream()
                .map(PopularKeyword::getKeyword)
                .collect(Collectors.toList());

        Set<AmazonProductResponseDto> amazonProductSet = new HashSet<>();
        for (String keyword : keywordList) {
            try {
                List<String> asinList = rainforestService.fetchProductIds(keyword, POPULAR_MAX_PAGE);
                List<AmazonProductResponseDto> resultList = asinList.stream()
                        .filter(asin -> !hwasProductService.isAlreadyRegistered(Vendor.AMAZON_US, asin))
                        .map(rainforestService::fetchProduct)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                amazonProductSet.addAll(resultList);
            } catch (RainforestException exception) {
                log.error("Exception occurred during sourcing popular keywords." + exception);
            }
        }
        List<AmazonProductResponseDto> amazonProductList = new ArrayList<>(amazonProductSet);
        List<AmazonSourcingProduct> sourcingProductList = createSourcingResults(amazonProductList);
        unlockIndicators();
        return new SourcingResultDto(sourcingProductList, keywordList, LocalDate.now());
    }

    // TODO : 횡단관심사 AOP Refactoring
    private AnalyzedResultDto createSourcingProducts(SelectScoreType selectScoreType, SelectScoreContext context, int sourcingCount) {
        try {
            if (isRunning()) {
                String message = getStatusMessage(selectScoreType);
                return new ErrorResultDto(new AlreadyWorkingException(message), LocalDate.now());
            }
            lockIndicators();
            updateForbiddenWordSet();
            List<String> searchWordList = new ArrayList<>();
            List<AmazonSourcingProduct> sourcingProductList = new ArrayList<>();
            while (sourcingProductList.size() < sourcingCount) {
                SelectScore selectScore = context.nextSelectScore();
                String originalSearchWord = selectScore.getSearchWord();
                String translatedSearchWord = papagoTextTranslator.translateText(originalSearchWord, TRANSLATE_FROM, TRANSLATE_TO);
                if (translatedSearchWord == null) {
                    continue;
                }
                searchWordList.add(originalSearchWord);
                List<String> productIdList = rainforestService.fetchProductIds(translatedSearchWord, DEFAULT_MAX_PAGE);
                log.info("ASIN List's size is {}. Fetched by keyword : {}", productIdList.size(), translatedSearchWord);
                List<AmazonSourcingProduct> sourcingProductSubList = new ArrayList<>();
                for (int i = 0; i < productIdList.size() && sourcingProductList.size() + sourcingProductSubList.size() < sourcingCount; i++) {
                    String productId = productIdList.get(i);
                    if (hwasProductService.isAlreadyRegistered(Vendor.AMAZON_US, productId)) {
                        log.error("Found product is already registered. ASIN : {}", productId);
                        continue;
                    }
                    AmazonProductResponseDto amazonProductResponseDto = rainforestService.fetchProduct(productId);
                    if (amazonProductResponseDto == null) {
                        continue;
                    }
                    AmazonSourcingProduct amazonSourcingProduct;
                    try {
                        amazonSourcingProduct = amazonSourcingProductFactory.create(amazonProductResponseDto);
                    } catch (Exception exception) {
                        log.error("An Exception occurred while create AmazonSourcingProduct for ASIN : {}", productId, exception);
                        continue;
                    }
                    if (!isValidProduct(amazonSourcingProduct)) {
                        log.error("ASIN : {}, product failed validation.", productId);
                        continue;
                    }
                    sourcingProductSubList.add(amazonSourcingProduct);
                    log.info("A product [ASIN : {}] is validated and added to sourcingProductList.", productId);
                }
                log.info("Sourcing new products done.(SearchWord : {}({}), size : {})", translatedSearchWord, originalSearchWord, sourcingProductSubList.size());
                sourcingProductList.addAll(sourcingProductSubList);
                log.info("Current sourcing-results list size : {}", sourcingProductList.size());
            }
            unlockIndicators();
            return new SourcingResultDto(sourcingProductList, searchWordList, LocalDate.now());
        } catch (Exception exception) {
            log.error("createSourcingProducts failed[SelectScoreType : {}]. Empty list returned.", selectScoreType, exception);
            unlockIndicators();
            return new ErrorResultDto(exception, LocalDate.now());
        }
    }

    public boolean isRunning() {
        return running.get();
    }

    public String getStatusMessage(SelectScoreType selectScoreType) {
        LocalDateTime now = LocalDateTime.now();
        long hours = ChronoUnit.HOURS.between(startedAt, now);
        long minutes = ChronoUnit.MINUTES.between(startedAt, now);
        String currentStatus = String.format("duration : %s hours %s minutes", hours, minutes);
        return String.format("ProductSourcingService[%s] is already working(%s)", selectScoreType, currentStatus);
    }

    public AnalyzedResultDto getForbiddenWords() {
        log.info("GET forbiddenWords.");
        List<ForbiddenWord> forbiddenWordList = forbiddenWordService.getAllForbiddenWords();
        return new ForbiddenWordResultDto(forbiddenWordList, LocalDate.now());
    }

    public AnalyzedResultDto addForbiddenWords(List<String> targetWords) {
        log.info("ADD forbiddenWords : [{}].", targetWords.toString());
        List<ForbiddenWord> forbiddenWordList = forbiddenWordService.saveForbiddenWords(targetWords);
        return new ForbiddenWordResultDto(forbiddenWordList, LocalDate.now());
    }

    public void deleteForbiddenWord(List<String> targetWords) {
        log.info("DELETE forbiddenWords : [{}].", targetWords.toString());
        forbiddenWordService.deleteForbiddenWords(targetWords);
    }

    public AnalyzedResultDto getPopularKeywords() {
        log.info("GET popularKeywords.");
        List<PopularKeyword> popularKeywordList = popularKeywordService.getAllPopularKeywords();
        return new PopularKeywordResultDto(popularKeywordList, LocalDate.now());
    }

    public AnalyzedResultDto addPopularKeywords(PopularKeywordRequestDto requestDto) {
        log.info("ADD popularKeywords : [{}].", requestDto.toString());
        List<PopularKeyword> popularKeywordList = popularKeywordService.savePopularKeywords(requestDto.getPopularKeywords());
        return new PopularKeywordResultDto(popularKeywordList, LocalDate.now());
    }

    public void deletePopularKeywords(List<String> targetWords) {
        log.info("DELETE forbiddenWords : [{}].", targetWords.toString());
        popularKeywordService.deletePopularKeywords(targetWords);
    }

    private boolean isValidProduct(SourcingProduct sourcingProduct) {
        SourcingItem item = sourcingProduct.getItems()
                .stream()
                .findFirst()
                .orElse(null);

        if (!containsEssentialData(item)) {
            log.error("SourcingItem has missing information.");
            return false;
        }

        if (!isValidKeyword(sourcingProduct, item.getTitle())) {
            log.error("SourcingItem contains forbidden keyword.");
            return false;
        }
        if (item.getPrice() < MINIMUM_PRICE_POLICY) {
            log.error("SourcingItem price({}) is lower than MINIMUM_PRICE_POLICY : {}", item.getPrice(), MINIMUM_PRICE_POLICY);
            return false;
        }
        if (item.getPrice() <= item.getDeliveryPrice()) {
            log.error("SourcingItem's delivery price is higher than SourcingItem price itself.");
            return false;
        }

        return true;
    }

    private boolean containsEssentialData(SourcingItem item) {
        return item != null
                && item.getTitle() != null && item.getPrice() != null && item.getDeliveryPrice() != null
                && item.getMainImage() != null && item.getImages() != null && !item.getImages().isEmpty()
                && item.getDimensions() != null && !item.getDimensions().isEmpty()
                && ((item.getAttributes() != null && !item.getAttributes().isEmpty()) || (item.getSpecifications() != null && !item.getSpecifications().isEmpty()));
    }

    private boolean isValidKeyword(SourcingProduct sourcingProduct, String productTitle) {
        List<String> validationRequired = List.of(sourcingProduct.getProductName(), sourcingProduct.getBrand(), productTitle);
        return validationRequired.stream()
                .allMatch(this::validateKeyword);
    }

    protected boolean validateKeyword(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return forbiddenWords.stream()
                .noneMatch(lowerCaseKeyword::contains);
    }

    private void updateForbiddenWordSet() {
        List<ForbiddenWord> forbiddenWordList = forbiddenWordService.getAllForbiddenWords();
        Set<String> updatedSet = forbiddenWordList.stream()
                .map(ForbiddenWord::getWord)
                .collect(Collectors.toSet());
        forbiddenWords.clear();
        forbiddenWords.addAll(updatedSet);
    }

    private List<AmazonSourcingProduct> createSourcingResults(List<AmazonProductResponseDto> amazonProductList) {
        List<AmazonSourcingProduct> sourcingProductList = new ArrayList<>();
        for (AmazonProductResponseDto amazonProduct : amazonProductList) {
            try {
                String productId = amazonProduct.getProductId();
                AmazonSourcingProduct amazonSourcingProduct = amazonSourcingProductFactory.create(amazonProduct);
                if (!isValidProduct(amazonSourcingProduct)) {
                    log.error("ASIN : {}, product failed validation.", productId);
                    throw new IllegalStateException();

                }
                sourcingProductList.add(amazonSourcingProduct);
                log.info("A product [ASIN : {}] is validated and added to sourcingProductList.", productId);
            } catch (Exception exception) {
                log.error("An Exception occurred while create AmazonSourcingProduct for ASIN : {}",
                        amazonProduct.getProductId(), exception);
            }
        }
        return sourcingProductList;
    }

    private void lockIndicators() {
        running.set(true);
        this.startedAt = LocalDateTime.now();
    }

    private void unlockIndicators() {
        running.set(false);
    }

}
