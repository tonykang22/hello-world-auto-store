package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.Currency;
import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.common.domain.Vendor;
import com.github.kingwaggs.productmanager.common.domain.dto.HelloWorldAutoStoreProductDto;
import com.github.kingwaggs.productmanager.common.domain.dto.TargetProductDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ProcessedProductResultDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ProductManagerResultDto;
import com.github.kingwaggs.productmanager.common.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.productmanager.common.domain.product.SourcingItem;
import com.github.kingwaggs.productmanager.common.domain.product.SourcingProduct;
import com.github.kingwaggs.productmanager.common.domain.product.StaleProducts;
import com.github.kingwaggs.productmanager.common.exception.ApiRequestException;
import com.github.kingwaggs.productmanager.common.service.HelloWorldAutoStoreProductService;
import com.github.kingwaggs.productmanager.common.util.DateTimeConverter;
import com.github.kingwaggs.productmanager.coupang.domain.CoupangPrice;
import com.github.kingwaggs.productmanager.coupang.domain.policy.CoupangDeliveryPolicy;
import com.github.kingwaggs.productmanager.coupang.domain.policy.CoupangProductItemPolicy;
import com.github.kingwaggs.productmanager.coupang.exception.LogisticsException;
import com.github.kingwaggs.productmanager.coupang.exception.ReturnShippingCenterException;
import com.github.kingwaggs.productmanager.coupang.sdk.config.CoupangVendorConfig;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.*;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.productmanager.coupang.sdk.util.ResourceBundleFactory;
import com.github.kingwaggs.productmanager.coupang.sdk.util.SellerProductCategoryValidator;
import com.github.kingwaggs.productmanager.deliveryagency.exception.DeliveryPriceException;
import com.github.kingwaggs.productmanager.util.URLShortener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangProductService {

    private static final int FIXED_PERIOD_OF_SELLING_IN_MONTHS = 6;

    private static final String ATTRIBUTE_MAIN_TYPE_NAME = "상품명";
    private static final int ATTRIBUTE_BEGIN_INDEX = 0;
    private static final int ATTRIBUTE_TYPE_NAME_MAX_LENGTH = 25;
    private static final int ATTRIBUTE_VALUE_NAME_MAX_LENGTH = 30;

    private static final String ITEM_NAME_DELIMITER = " - ";
    private static final int ITEM_NAME_BEGIN_INDEX = 0;
    private static final int ITEM_NAME_MAX_LENGTH = 100;

    private static final String REQUIRED_DOCUMENT_TEMPLATE_NAME = "기타인증서류";
    private static final int IMAGE_VENDOR_PATH_MAX_LENGTH = 200;
    private static final int DOCUMENT_VENDOR_PATH_MAX_LENGTH = 150;

    private static final String FIXED_NOTICE_CONTENT = "상세페이지 참조";
    private static final String HTML_NEW_LINE = "<br>";

    private final CoupangVendorConfig vendorConfig;
    private final CoupangCategoryService categoryService;
    private final CoupangPriceService priceService;
    private final CoupangLogisticsService logisticsService;
    private final CoupangProductStatusFetcher productStatusFetcher;
    private final URLShortener urlShortener;
    private final HelloWorldAutoStoreProductService hwasProductService;
    private final CoupangMarketPlaceApi coupangApiInstance;

    @Value("${welcome.image.path}")
    private List<String> welcomeImagePathList;

    @Value("${goodbye.image.path}")
    private List<String> goodbyeImagePathList;

    @Value("${coupang.product.automatic.approval}")
    private boolean isAutomaticApproval;

    public String createProduct(SourcingProduct sourcingProduct) {
        try {
            SourcingItem sourcingItem = sourcingProduct.getItems().stream().findFirst().orElseGet(() -> null);
            if (sourcingItem == null || hwasProductService.isAlreadyRegistered(sourcingProduct.getSourcingVendor(), sourcingItem.getItemId())) {
                log.info("Sourcing item is null or it's already registered. Return null.");
                return null;
            }
            OSellerProduct oSellerProduct = new OSellerProduct();
            AutoCategorizationResponseDto autoCategory = categoryService.createAutoCategory(sourcingProduct);
            ONoticeCategory categoryNotices = categoryService.createCategoryNotices(autoCategory.getPredictedCategoryId());
            String productName = createProductName(sourcingProduct);

            mappingProductCoreProperties(oSellerProduct, sourcingProduct, productName, autoCategory);
            mappingDeliveryProperties(oSellerProduct);
            mappingVendorProperties(oSellerProduct);
            mappingReturnProperties(oSellerProduct);
            mappingMetaProperties(oSellerProduct, sourcingProduct);

            List<OSellerProductItem> productItemList = createProductItemList(sourcingProduct, sourcingItem, productName, categoryNotices);
            if (productItemList == null) {
                log.info("Product item list is null. One of required args could be null or empty. Return null.");
                return null;
            }
            oSellerProduct.setItems(productItemList);

            // "validation_message" is the base name of the resource bundle to localize the validation message in both Korean and English
            ResourceBundle resourceBundle = ResourceBundleFactory.createResourceBundle("validation_message", vendorConfig.getDisplayedLanguage());

            // Validate OSellerProduct
            SellerProductCategoryValidator.validateSellerProduct(
                    vendorConfig.getVendorId(),
                    Long.valueOf(autoCategory.getPredictedCategoryId()),
                    vendorConfig.getSecretKey(),
                    vendorConfig.getAccessKey(),
                    oSellerProduct,
                    resourceBundle
            );

            // Invoke API
            OpenApiResultOflong createResult = coupangApiInstance.createSellerProduct(vendorConfig.getVendorId(), oSellerProduct);
            if (!createResult.getCode().equals("SUCCESS")) {
                log.error("Request to Coupang to create product FAILED. Return null. (result : {})", createResult);
                return null;
            }
            OSellerProductItem oSellerProductItem = oSellerProduct.getItems().stream().findFirst().get();
            HelloWorldAutoStoreProductDto dto = createHelloWorldAutoStoreProductDto(oSellerProduct, oSellerProductItem, sourcingItem, categoryNotices, Vendor.AMAZON_US, Vendor.COUPANG_KR);
            String sourceId = getSourceId(sourcingProduct);
            String destinationId = getDestinationId(createResult);
            SaleStatus saleStatus = productStatusFetcher.getStatus(destinationId);
            HelloWorldAutoStoreProduct hwasProduct = hwasProductService.saveProductFrom(dto, sourceId, destinationId, saleStatus);
            return hwasProduct.getDestinationId();
        } catch (Exception exception) {
            log.error("Unexpected Error occured. Return null. (exception : {})", Arrays.toString(exception.getStackTrace()));
            return null;
        }
    }

    public ProductManagerResultDto deleteStaleProduct(LocalDate targetDate) {
        StaleProducts staleProducts = hwasProductService.getCoupangStaleProductList(targetDate);
        List<String> staleProductIdList = staleProducts.getStaleProductList().stream().map(HelloWorldAutoStoreProduct::getDestinationId).collect(Collectors.toList());
        List<String> deletedProductIdList = deleteProductByProductIdList(staleProductIdList);
        return new ProcessedProductResultDto(deletedProductIdList, staleProducts.getStaleDateTime());
    }

    public ProductManagerResultDto deleteProduct(TargetProductDto targetProductDto) throws InvalidParameterException {
        List<HelloWorldAutoStoreProduct> toBeDeleted = hwasProductService.getHwasProductListBy(targetProductDto);
        List<String> toBeDeletedIdList = toBeDeleted.stream().map(HelloWorldAutoStoreProduct::getDestinationId).collect(Collectors.toList());
        List<String> deletedProductIdList = deleteProductByProductIdList(toBeDeletedIdList);
        return new ProcessedProductResultDto(deletedProductIdList, targetProductDto.getDateTime());
    }

    public List<String> deleteProductByProductIdList(List<String> productIdList) {
        List<String> deletedProductIdList = new ArrayList<>();
        for (String productId : productIdList) {
            try {
                deleteProductByProductId(productId);
                log.info("Delete seller product SUCCESS. (Deleted Coupang Product ID : {})", productId);
                deletedProductIdList.add(productId);
            } catch (Exception exception) {
                log.error("Delete seller product FAILED. Skip to delete product from Coupang. (Coupang Product ID : {})", productId);
            }
        }
        return deletedProductIdList;
    }

    private String createProductName(SourcingProduct sourcingProduct) {
        String sellerProductName = sourcingProduct.getProductName();
        sellerProductName = sellerProductName.replaceAll(sourcingProduct.getBrand(), "").trim();
        return sellerProductName.substring(ITEM_NAME_BEGIN_INDEX, Math.min(sellerProductName.length(), ITEM_NAME_MAX_LENGTH));
    }

    private void deleteProductByProductId(String productId) throws ApiException, InterruptedException {
        // 상품의 vendorItemId 조회
        Long vendorItemId = getVendorItemId(productId);
        if (vendorItemId != null) {
            stopSellingVendorItem(vendorItemId);
            hwasProductService.updateCoupangProductStatus(productId, SaleStatus.OFF);
        }
        deleteSellerProduct(productId);
    }

    private Long getVendorItemId(String productId) throws ApiException {
        OpenApiResultOfOSellerProduct readSellerProductResult = coupangApiInstance.readSellerProduct(vendorConfig.getVendorId(), productId);
        OSellerProduct oSellerProduct = readSellerProductResult.getData();
        return oSellerProduct.getItems().stream().findFirst().get().getVendorItemId();
    }

    private void deleteSellerProduct(String productId) throws ApiException, InterruptedException {
        // delete by sellerProductId
        OpenApiResult deleteSellerProductResult = coupangApiInstance.deleteSellerProduct(vendorConfig.getVendorId(), productId);
        Thread.sleep(100);
        if (!deleteSellerProductResult.getCode().equals("SUCCESS")) {
            throw new ApiException(String.format("Delete Seller Product request FAILED. Skip to delete this product from Coupang. (productId : %s)", productId));
        }
    }

    // TODO : Exception
    private void stopSellingVendorItem(Long vendorItemId) throws ApiException, InterruptedException {
        // vendorItem 판매중지
        OpenApiResult stopSellingResult = coupangApiInstance.stopSelling(vendorConfig.getVendorId(), vendorItemId);
        Thread.sleep(1000);
        if (!stopSellingResult.getCode().equals("SUCCESS")) {
            log.error("Stop selling request FAILED. (vendorItemId : {})", vendorItemId);
//            throw new ApiException(String.format("Stop selling request FAILED. (vendorItemId : %s)", vendorItemId));
        }
    }

    private HelloWorldAutoStoreProductDto createHelloWorldAutoStoreProductDto(OSellerProduct oSellerProduct, OSellerProductItem oSellerProductItem, SourcingItem sourcingItem, ONoticeCategory categoryNotices, Vendor source, Vendor destination) {
        return HelloWorldAutoStoreProductDto.builder()
                .brand(oSellerProduct.getBrand())
                .name(oSellerProduct.getSellerProductName())
                .source(source)
                .sourceOriginalPrice(sourcingItem.getPrice())
                .stockCount(sourcingItem.getAvailability())
                .sourceCurrency(Currency.USD.toString())
                .destination(destination)
                .destinationOriginalPrice(oSellerProductItem.getOriginalPrice())
                .destinationSalePrice(oSellerProductItem.getSalePrice())
                .destinationCurrency(Currency.KRW.toString())
                .category(categoryNotices.getNoticeCategoryName())
                .saleStartedAt(oSellerProduct.getSaleStartedAt())
                .saleEndedAt(oSellerProduct.getSaleEndedAt())
                .build();
    }

    private String getDestinationId(OpenApiResultOflong result) {
        return result.getData().toString();
    }

    private String getSourceId(SourcingProduct sourcingProduct) {
        SourcingItem sourcingItem = sourcingProduct.getItems().stream().findFirst().get();
        return sourcingItem.getItemId();
    }

    private List<OSellerProductItem> createProductItemList(SourcingProduct sourcingProduct, SourcingItem sourcingItem, String productName, ONoticeCategory categoryNotices) {
        try {
            List<OSellerProductItem> productItemList = new ArrayList<>();
            // Populate item
            OSellerProductItem productItem = new OSellerProductItem();
            mappingProductItemPolicies(productItem);

            int maximumBuyCount = sourcingItem.getAvailability();
            productItem.setMaximumBuyCount(maximumBuyCount);

            productItem.setItemName(productName);

            CoupangPrice coupangPrice = priceService.createPrices(sourcingItem);
            productItem.setOriginalPrice(coupangPrice.getOriginalPrice());
            productItem.setSalePrice(coupangPrice.getSalePrice());

            productItem.setExternalVendorSku(sourcingItem.getExternalVendorSku());

            // Add search tags for item
            List<String> keywordListByFrequency = createKeywordListByFrequency(sourcingProduct.getKeywordsList());
            productItem.setSearchTags(keywordListByFrequency);

            // Populate images for item
            List<OSellerProductItemImage> productItemImages = createProductItemImages(sourcingItem);
            if (productItemImages == null) {
                return null;
            }
            productItem.setImages(productItemImages);

            // Populate attributes for item
            List<OSellerProductItemAttribute> productItemAttributes = createProductItemAttributes(sourcingItem);
            // 단일 상품만 등록하는 것으로 결정하여 빈 리스트 주입
            productItem.setAttributes(new ArrayList<>());

            // Populate contents for item
            List<OSellerProductItemContent> contentList = createContentList(sourcingProduct, sourcingItem);
            productItem.setContents(contentList);

            List<OSellerProductItemNotice> noticeList = createNoticeList(categoryNotices);
            productItem.setNotices(noticeList);

            productItemList.add(productItem);

            return productItemList;
        } catch (DeliveryPriceException deliveryPriceException) {
            log.error("{} occured. Return null. (message : {}, itemId : {})",
                    deliveryPriceException.getClass().getSimpleName(),
                    deliveryPriceException.getLocalizedMessage(),
                    sourcingItem.getItemId());
            return null;
        } catch (Exception e) {
            log.error("Unexpected Error occured while createProductItemList. Return null.", e);
            return null;
        }
    }

    private List<String> createKeywordListByFrequency(List<String> keywordsList) {
        Map<String, Integer> frequencyMap = new LinkedHashMap<>();
        for (String keyword : keywordsList) {
            String[] split = keyword.split(" ");
            for (String word : split) {
                frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
            }
        }
        List<String> sortedKeywordList = frequencyMap.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return sortedKeywordList.subList(0, Math.min(sortedKeywordList.size(), 20));
    }

    private List<OSellerProductItemNotice> createNoticeList(ONoticeCategory categoryNotices) {
        List<OSellerProductItemNotice> noticeList = new ArrayList<>();
        for (ONoticeCategoryTemplate template : categoryNotices.getNoticeCategoryDetailNames()) {
            OSellerProductItemNotice notice = new OSellerProductItemNotice();
            notice.setNoticeCategoryName(categoryNotices.getNoticeCategoryName());
            notice.setNoticeCategoryDetailName(template.getNoticeCategoryDetailName());
            notice.setContent(FIXED_NOTICE_CONTENT);
            noticeList.add(notice);
        }
        return noticeList;
    }

    private List<OSellerProductItemContent> createContentList(SourcingProduct sourcingProduct, SourcingItem sourcingItem) {
        List<OSellerProductItemContent> totalContentList = new ArrayList<>();

        // welcome img list
        OSellerProductItemContent welcomeImageContentList = createItemImageContentList(welcomeImagePathList);
        totalContentList.add(welcomeImageContentList);

        // content img list
        List<String> itemImagePathList = sourcingItem.getImages();
        OSellerProductItemContent itemImageContentList = createItemImageContentList(itemImagePathList);
        totalContentList.add(itemImageContentList);

        // html img list - 상품과 무관한 판매중이지 않은 상품 이미지를 본문에 달아 오해의 소지가 발생할 수 있어 제외
//        List<String> htmlImagePathList = createHtmlImageList(sourcingProduct.getDetailedDescriptionPage());
//        if (!htmlImagePathList.isEmpty()) {
//            OSellerProductItemContent htmlImageContentList = createItemImageContentList(htmlImagePathList);
//            totalContentList.add(htmlImageContentList);
//        }

        // html content
        OSellerProductItemContent htmlContentList = createHtmlContentList(sourcingProduct, sourcingItem);
        totalContentList.add(htmlContentList);

        // goodbye img list
        OSellerProductItemContent goodbyeImageContentList = createItemImageContentList(goodbyeImagePathList);
        totalContentList.add(goodbyeImageContentList);

        return totalContentList;
    }

    private String filterDescriptionPage(String html) {
        Document descriptionPage = Jsoup.parse(html);
        descriptionPage.select("h2").remove();
        descriptionPage.select("div").stream().findFirst().ifPresent(element -> element.getElementsByAttributeValueContaining("class", "brand-story").remove());
        String filtered = descriptionPage.outerHtml();
        return replaceHtmlKeyword(filtered);
    }

    private String replaceHtmlKeyword(String html) {
        return html
                .replaceAll("더 읽기", "")
                .replaceAll("더 보기", "");
    }

    private OSellerProductItemContent createHtmlContentList(SourcingProduct sourcingProduct, SourcingItem sourcingItem) {
        OSellerProductItemContent htmlContentList = new OSellerProductItemContent();
        htmlContentList.setContentsType(OSellerProductItemContent.ContentsTypeEnum.HTML);

        List<OVendorInventoryItemContentDetail> htmlContentDetailList = new ArrayList<>();
        String detailedDescriptionPage = sourcingProduct.getDetailedDescriptionPage();
        if (detailedDescriptionPage != null) {
            String descriptionPage = filterDescriptionPage(detailedDescriptionPage);
            setContentDetail(htmlContentDetailList, descriptionPage);
            setContentDetail(htmlContentDetailList, HTML_NEW_LINE);
        }

        List<String> featureBullets = sourcingProduct.getFeatureBullets();
        for (String featureBullet : featureBullets) {
            if (containsBadKeyword(featureBullet)) {
                continue;
            }
            setContentDetail(htmlContentDetailList, featureBullet);
            setContentDetail(htmlContentDetailList, HTML_NEW_LINE);
        }

        String description = sourcingProduct.getDescription();
        if (description != null) {
            String htmlDescription = createHtmlDescription(description);
            setContentDetail(htmlContentDetailList, htmlDescription);
            setContentDetail(htmlContentDetailList, HTML_NEW_LINE);
        }

        Map<String, String> detailEntryMap = new LinkedHashMap<>();
        addDetailEntry(detailEntryMap, sourcingItem.getAttributes());
        addDetailEntry(detailEntryMap, sourcingItem.getSpecifications());
        for (var entry : detailEntryMap.entrySet()) {
            String detailRow = createDetailRowFromEntry(entry);
            setContentDetail(htmlContentDetailList, detailRow);
        }
        setContentDetail(htmlContentDetailList, HTML_NEW_LINE);
        htmlContentList.setContentDetails(htmlContentDetailList);
        return htmlContentList;
    }

    private String createHtmlDescription(String description) {
        description = description.replaceAll("질문과 대답", "FAQ");
        description = description.replaceAll("브랜드에서", "");
        description = description.replaceAll("이전 페이지", "");
        description = description.replaceAll("다음 페이지", "");
        description = description.replaceAll("제조원으로부터", "");
        description = description.replaceAll("\n\n", HTML_NEW_LINE);
        return description;
    }

    private List<String> createHtmlImageList(String html) {
        if (html == null) {
            return new ArrayList<>();
        }
        Set<String> htmlImageSet = new LinkedHashSet<>();
        Document document;
        //Get Document object after parsing the html from given url.
        document = Jsoup.parse(html);

        //Get images from document object.
        Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

        //Iterate images and print image attributes.
        for (Element image : images) {
            htmlImageSet.add(image.attr("src"));
        }
        return new ArrayList<>(htmlImageSet);
    }

    private OSellerProductItemContent createItemImageContentList(List<String> resources) {
        OSellerProductItemContent imageContentList = new OSellerProductItemContent();
        imageContentList.setContentsType(OSellerProductItemContent.ContentsTypeEnum.IMAGE);

        List<OVendorInventoryItemContentDetail> imageContentDetailList = new ArrayList<>();
        for (String resource : resources) {
            OVendorInventoryItemContentDetail contentDetail = new OVendorInventoryItemContentDetail();
            contentDetail.setDetailType(OVendorInventoryItemContentDetail.DetailTypeEnum.IMAGE);
            contentDetail.setContent(resource);
            imageContentDetailList.add(contentDetail);
        }
        imageContentList.setContentDetails(imageContentDetailList);
        return imageContentList;
    }

    private void addDetailEntry(Map<String, String> detailEntryMap, Map<String, String> detailEntries) {
        for (var entry : detailEntries.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (containsBadKeyword(key) || containsBadKeyword(value)) {
                continue;
            }
            if (detailEntryMap.containsKey(key)) {
                String usedValue = detailEntryMap.get(key);
                value = value.length() > usedValue.length() ? value : usedValue;
            }
            detailEntryMap.put(key, value);
        }
    }

    private boolean containsBadKeyword(String key) {
        return key.contains("ASIN")
                || key.contains("죄악")
                || key.contains("처음 사용 가능한 날짜")
                || key.contains("원산지")
                || key.contains("고객 리뷰")
                || key.contains("베스트셀러 순위")
                || key.contains("환불")
                || key.contains("교환")
                || key.contains("서비스")
                || key.contains("무상")
                || key.contains("보증")
                || key.contains("애프터")
                || key.contains("반품");
    }

    private String createDetailRowFromEntry(Map.Entry<String, String> entry) {
        String message = String.join(" : ", entry.getKey(), entry.getValue());
        return String.format(" · %s", message);
    }

    private void setContentDetail(List<OVendorInventoryItemContentDetail> details, String detail) {
        OVendorInventoryItemContentDetail contentDetail = new OVendorInventoryItemContentDetail();
        contentDetail.setDetailType(OVendorInventoryItemContentDetail.DetailTypeEnum.TEXT);
        contentDetail.setContent(detail);
        details.add(contentDetail);
    }

    private List<OSellerProductItemAttribute> createProductItemAttributes(SourcingItem sourcingItem) {
        List<OSellerProductItemAttribute> productItemAttributes = new ArrayList<>();
        String itemTitle = sourcingItem.getTitle();
        String shortenItemTitle = itemTitle.substring(ATTRIBUTE_BEGIN_INDEX, Math.min(itemTitle.length(), ATTRIBUTE_VALUE_NAME_MAX_LENGTH));
        Set<String> usedAttributeSet = new HashSet<>();
        setMainAttribute(productItemAttributes, shortenItemTitle, usedAttributeSet);
        setMetaAttributes(productItemAttributes, sourcingItem.getAttributes(), usedAttributeSet);
        setMetaAttributes(productItemAttributes, sourcingItem.getSpecifications(), usedAttributeSet);
        return productItemAttributes;
    }

    private void setMainAttribute(List<OSellerProductItemAttribute> productItemAttributes, String attribute, Set<String> usedAttributeSet) {
        OSellerProductItemAttribute itemAttributeTitle = new OSellerProductItemAttribute();
        if (usedAttributeSet.contains(ATTRIBUTE_MAIN_TYPE_NAME)) {
            return;
        }
        itemAttributeTitle.setAttributeTypeName(ATTRIBUTE_MAIN_TYPE_NAME);
        itemAttributeTitle.setAttributeValueName(attribute);
        productItemAttributes.add(itemAttributeTitle);
    }

    private void setMetaAttributes(List<OSellerProductItemAttribute> productItemAttributes, Map<String, String> attributeMap, Set<String> usedAttributeSet) {
        for (var entry : attributeMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String shortenKey = key.substring(ATTRIBUTE_BEGIN_INDEX, Math.min(key.length(), ATTRIBUTE_TYPE_NAME_MAX_LENGTH));
            String shortenValue = value.substring(ATTRIBUTE_BEGIN_INDEX, Math.min(value.length(), ATTRIBUTE_VALUE_NAME_MAX_LENGTH));
            if (usedAttributeSet.contains(shortenKey)) {
                continue;
            }
            OSellerProductItemAttribute productItemAttribute = createProductItemAttribute(shortenKey, shortenValue);
            productItemAttributes.add(productItemAttribute);
            usedAttributeSet.add(shortenKey);
        }
    }

    private OSellerProductItemAttribute createProductItemAttribute(String key, String value) {
        OSellerProductItemAttribute itemAttributeMeta = new OSellerProductItemAttribute();
        itemAttributeMeta.setAttributeTypeName(key);
        itemAttributeMeta.setAttributeValueName(value);
        itemAttributeMeta.setExposed(OSellerProductItemAttribute.ExposedEnum.NONE);
        return itemAttributeMeta;
    }

    private void mappingProductItemPolicies(OSellerProductItem productItem) {
        productItem.setMaximumBuyForPerson(CoupangProductItemPolicy.MAXIMUM_BUY_FOR_PERSON);
        productItem.setMaximumBuyForPersonPeriod(CoupangProductItemPolicy.MAXIMUM_BUY_FOR_PERSON_PERIOD);
        productItem.setOutboundShippingTimeDay(CoupangProductItemPolicy.OUTBOUND_SHIPPING_TIME_DAY);
        productItem.setUnitCount(CoupangProductItemPolicy.UNIT_COUNT);
        productItem.setAdultOnly(CoupangProductItemPolicy.ADULT_ONLY);
        productItem.setTaxType(CoupangProductItemPolicy.TAX_TYPE);
        productItem.setParallelImported(CoupangProductItemPolicy.PARALLEL_IMPORTED);
        productItem.setOverseasPurchased(CoupangProductItemPolicy.OVERSEAS_PURCHASED);
        productItem.setPccNeeded(CoupangProductItemPolicy.PCC_NEEDED);
    }

    private void mappingProductCoreProperties(OSellerProduct oSellerProduct, SourcingProduct sourcingProduct, String productName, AutoCategorizationResponseDto autoCategory) {
        Long displayCategoryCode = Long.valueOf(autoCategory.getPredictedCategoryId());
        oSellerProduct.setDisplayCategoryCode(displayCategoryCode);

        oSellerProduct.setSellerProductName(productName);

        LocalDateTime saleStartedAt = LocalDateTime.now();
        LocalDateTime saleEndedAt = saleStartedAt.plusMonths(FIXED_PERIOD_OF_SELLING_IN_MONTHS);
        oSellerProduct.setSaleStartedAt(DateTimeConverter.localDateTime2DateTimeString(saleStartedAt));
        oSellerProduct.setSaleEndedAt(DateTimeConverter.localDateTime2DateTimeString(saleEndedAt));

        String brand = sourcingProduct.getBrand();
        oSellerProduct.setBrand(brand);
        oSellerProduct.setManufacture(brand);
    }

    private void mappingReturnProperties(OSellerProduct oSellerProduct) throws LogisticsException {
        ShippingPlaceResponseReturnDto returnShippingCenter = logisticsService.getReturnShippingCenter();
        oSellerProduct.setReturnCenterCode(returnShippingCenter.getReturnCenterCode());
        oSellerProduct.setReturnChargeName(returnShippingCenter.getShippingPlaceName());

        PlaceAddressDto returnShippingCenterAddress = returnShippingCenter
                .getPlaceAddresses()
                .stream()
                .findFirst()
                .orElseThrow(ReturnShippingCenterException::new);
        oSellerProduct.setCompanyContactNumber(returnShippingCenterAddress.getCompanyContactNumber());
        oSellerProduct.setReturnZipCode(returnShippingCenterAddress.getReturnZipCode());
        oSellerProduct.setReturnAddress(returnShippingCenterAddress.getReturnAddress());
        oSellerProduct.setReturnAddressDetail(returnShippingCenterAddress.getReturnAddressDetail());
        // ReturnChargeVendor 가 뭔지 설명이 나와있지 않음
        // oSellerProduct.setReturnChargeVendor(OSellerProduct.ReturnChargeVendorEnum.N);

        OutboundInquiryReturn outboundShippingPlace = logisticsService.getOutboundShippingPlace();
        oSellerProduct.setOutboundShippingPlaceCode(outboundShippingPlace.getOutboundShippingPlaceCode());
    }

    private void mappingDeliveryProperties(OSellerProduct oSellerProduct) {
        oSellerProduct.setDeliveryMethod(CoupangDeliveryPolicy.DELIVERY_METHOD);
        oSellerProduct.setDeliveryCompanyCode(CoupangDeliveryPolicy.DELIVERY_COMPANY_CODE);
        oSellerProduct.setDeliveryChargeType(CoupangDeliveryPolicy.DELIVERY_CHARGE_TYPE);
        oSellerProduct.setDeliveryCharge(CoupangDeliveryPolicy.DELIVERY_CHARGE);
        oSellerProduct.setFreeShipOverAmount(CoupangDeliveryPolicy.FREE_SHIP_OVER_AMOUNT);
        oSellerProduct.setRemoteAreaDeliverable(CoupangDeliveryPolicy.REMOTE_AREA_DELIVERABLE);
        oSellerProduct.setUnionDeliveryType(CoupangDeliveryPolicy.UNION_DELIVERY_TYPE);
        oSellerProduct.setDeliveryChargeOnReturn(CoupangDeliveryPolicy.DELIVERY_CHARGE_ON_RETURN);
        oSellerProduct.setReturnCharge(CoupangDeliveryPolicy.RETURN_CHARGE);
    }

    private void mappingVendorProperties(OSellerProduct oSellerProduct) {
        oSellerProduct.setVendorId(vendorConfig.getVendorId());
        oSellerProduct.setVendorUserId(vendorConfig.getVendorUserId());
    }

    private List<OSellerProductItemImage> createProductItemImages(SourcingItem sourcingItem) {
        List<OSellerProductItemImage> itemImages = new ArrayList<>();

        List<String> resources = sourcingItem.getImages();
        String mainImage = resources.stream().findFirst().orElseGet(() -> null);
        if (mainImage == null) {
            return null;
        }
        OSellerProductItemImage itemImage = new OSellerProductItemImage();
        OSellerProductItemImage.ImageTypeEnum imageType = OSellerProductItemImage.ImageTypeEnum.REPRESENTATION;
        String vendorPath;
        try {
            vendorPath = getValidVendorPath(mainImage, IMAGE_VENDOR_PATH_MAX_LENGTH);
        } catch (ApiRequestException e) {
            log.error("Exception occurred while shortening Image url. Return null.", e);
            return null;
        }
        itemImage.setImageOrder(0L);
        itemImage.setImageType(imageType);
        itemImage.setVendorPath(vendorPath);
        itemImages.add(itemImage);

        return itemImages;
    }


    private void mappingMetaProperties(OSellerProduct oSellerProduct, SourcingProduct sourcingProduct) {
        // true - 자동으로 등록 바로 넘어감, false - 작성 내용만 저장하고 판매요청 전 상태 (판매를 원할 시에는 상품 승인요청 API 또는 wing에서 판매요청을 진행 해야 함)
        oSellerProduct.setRequested(isAutomaticApproval);
        // 필수 서류 여부
        List<OSellerProductRequiredDocument> requiredDocumentList = new ArrayList<>();
        List<String> documents = sourcingProduct.getDocuments();
        for (String document : documents) {
            OSellerProductRequiredDocument requiredDocument = new OSellerProductRequiredDocument();
            requiredDocument.setTemplateName(REQUIRED_DOCUMENT_TEMPLATE_NAME);
            String documentPath;
            try {
                documentPath = getValidVendorPath(document, DOCUMENT_VENDOR_PATH_MAX_LENGTH);
            } catch (ApiRequestException apiRequestException) {
                log.info(apiRequestException.toString());
                continue;
            }
            requiredDocument.setVendorDocumentPath(documentPath);
            requiredDocumentList.add(requiredDocument);
        }
        oSellerProduct.setRequiredDocuments(requiredDocumentList);
    }

    private String getValidVendorPath(String path, int limit) throws ApiRequestException {
        if (path.length() > limit) {
            return urlShortener.getShortenURL(path);
        }
        return path;
    }

}
