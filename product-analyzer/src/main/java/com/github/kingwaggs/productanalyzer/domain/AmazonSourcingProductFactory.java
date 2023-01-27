package com.github.kingwaggs.productanalyzer.domain;

import com.github.kingwaggs.productanalyzer.domain.dto.response.AmazonProductResponseDto;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productanalyzer.exception.DeliveryPriceException;
import com.github.kingwaggs.productanalyzer.exception.OutOfStockException;
import com.github.kingwaggs.productanalyzer.service.productsourcing.DeliveryAgencyService;
import com.github.kingwaggs.productanalyzer.service.productsourcing.PapagoTextTranslator;
import com.github.kingwaggs.productanalyzer.service.productsourcing.PapagoWebPageTranslator;
import com.google.gson.internal.LinkedHashTreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonSourcingProductFactory {
    private static final String VALID_WORD_REGEX = "[^a-zA-Z0-9가-힣]+";
    private static final String TRANSLATE_FROM = "en";
    private static final String TRANSLATE_TO = "ko";
    private static final int MAXIMUM_BUY_COUNT = 9999;
    private static final double OUNCE_TO_POUND = 0.0625;

    private final PapagoTextTranslator papagoTextTranslator;
    private final PapagoWebPageTranslator papagoWebPageTranslator;
    private final DeliveryAgencyService deliveryAgencyService;

    public AmazonSourcingProduct create(AmazonProductResponseDto dto) throws OutOfStockException {
        String transedDescription = transDescription(dto);
        List<String> transedFeatureBullets = transFeatureBullets(dto);
        List<AmazonSourcingProduct.Category> categories = transCategories(dto);
        String brand = dto.getBrand();
        List<String> videos = transVideos(dto);
        List<String> documents = transDocuments(dto);
        List<AmazonSourcingProduct.Variant> variants = transVariants(dto);
        String productName = createProductName(brand, dto);
        List<String> transedKeywordsList = transKeywordsList(dto, categories);
        String detailedDescriptionPage = createDetailedDescriptionPage(dto);
        return AmazonSourcingProduct.builder()
                .productName(productName)
                .description(transedDescription)
                .featureBullets(transedFeatureBullets)
//                .keywordsList(keywordsList)
                .keywordsList(transedKeywordsList)
                .brand(brand)
                .videos(videos)
                .categories(categories)
                .documents(documents)
                .variants(variants)
                .detailedDescriptionPage(detailedDescriptionPage)
                .build();
    }

    private String transDescription(AmazonProductResponseDto dto) {
        String originalDescription = dto.getDescription();
        return translateEngToKor(originalDescription);
    }

    private List<String> transFeatureBullets(AmazonProductResponseDto dto) {
        List<String> originalFeatureBullets = dto.getFeatureBullets();
        return translateEngToKor(originalFeatureBullets);
    }

    private String createDetailedDescriptionPage(AmazonProductResponseDto dto) {
        return Optional.ofNullable(dto)
                .map(AmazonProductResponseDto::getProduct)
                .map(AmazonProductResponseDto.Product::getPlusContent)
                .map(AmazonProductResponseDto.PlusContent::getBody)
                .map(html -> {
                    String translatedHtml = papagoWebPageTranslator.translateWebPage(html, TRANSLATE_FROM, TRANSLATE_TO);
                    translatedHtml = translatedHtml.replaceAll("ASIN : [^A-Z0-9]+", "");
                    translatedHtml = translatedHtml.replaceAll("죄악 : [^A-Z0-9]+", "");
                    translatedHtml = translatedHtml.replace("다음 페이지", "");
                    translatedHtml = translatedHtml.replace("이전 페이지", "");
                    translatedHtml = translatedHtml.replace("브랜드에서", "");
                    return translatedHtml;
                })
                .orElseGet(() -> null);
    }

    private String createProductName(String brand, AmazonProductResponseDto amazonProductResponseDto) {
        AmazonProductResponseDto.Product product = amazonProductResponseDto.getProduct();
        if (product == null) {
            return null;
        }
        String title = product.getTitle();
        if (title == null) {
            return null;
        }
        String englishTitle = title.replace(brand, "");
        String koreanTitle = Objects.requireNonNull(translateEngToKor(englishTitle)).trim();
        String mixedTitle = String.join(" ", brand, koreanTitle);
        String finalTitle = mixedTitle.replace("%", "퍼센트");
        return String.join(" ", finalTitle);
    }

    private List<AmazonSourcingProduct.Variant> transVariants(AmazonProductResponseDto dto) throws OutOfStockException {
        return Collections.singletonList(transVariant(dto));
    }

    private List<String> transDocuments(AmazonProductResponseDto dto) {
        List<AmazonProductResponseDto.Document> documents = dto.getDocuments();
        return (documents == null)
                ? null
                : documents.stream()
                .map(AmazonProductResponseDto.Document::getLink)
                .collect(Collectors.toList());
    }

    private List<String> transVideos(AmazonProductResponseDto dto) {
        List<AmazonProductResponseDto.Video> videos = dto.getVideos();
        return (videos == null)
                ? null
                : videos.stream()
                .map(AmazonProductResponseDto.Video::getLink)
                .collect(Collectors.toList());
    }

    private Map<String, String> transSpecifications(AmazonProductResponseDto dto) {
        return mapNameValue(dto.getSpecifications());
    }

    private Map<String, String> transAttributes(AmazonProductResponseDto dto) {
        return mapNameValue(dto.getAttributes());
    }

    private List<AmazonSourcingProduct.Category> transCategories(AmazonProductResponseDto dto) {
        List<AmazonProductResponseDto.Category> originalCategoryList = dto.getCategoryList();
        if (originalCategoryList == null) {
            return null;
        }
        return originalCategoryList.stream()
                .map(c -> AmazonSourcingProduct.Category.builder()
                        .name(translateEngToKor(c.getName()))
                        .link(c.getLink())
                        .categoryId(c.getCategoryId())
                        .build())
                .collect(Collectors.toList());
    }

    private List<String> transKeywordsList(AmazonProductResponseDto dto, List<AmazonSourcingProduct.Category> categories) {
        Set<String> keywordSet = Optional.ofNullable(dto)
                .map(AmazonProductResponseDto::getKeywordsList).orElseGet(Collections::emptyList)
                .stream()
                .map(keyword -> keyword.replaceAll(VALID_WORD_REGEX, ""))
                .map(this::translateEngToKor)
                .collect(Collectors.toSet());

        if (categories != null && !categories.isEmpty()) {
            Set<String> categorySet = categories.stream()
                    .map(AmazonSourcingProduct.Category::getName)
                    .map(name -> name.replaceAll(VALID_WORD_REGEX, ""))
                    .collect(Collectors.toSet());
            keywordSet.addAll(categorySet);
        }

        return new ArrayList<>(keywordSet);
    }

    private List<Double> transDimensions(AmazonProductResponseDto dto) {
        String dimensions = dto.getDimensions();
        if (dimensions == null) {
            List<AmazonProductResponseDto.NameValue> specifications = dto.getSpecifications();
            List<AmazonProductResponseDto.NameValue> dimensionsEntry = specifications.stream().filter(nv -> nv.getName().contains("Dimensions")).collect(Collectors.toList());
            if (dimensionsEntry.isEmpty()) {
                return null;
            } else {
                dimensions = dimensionsEntry.stream().findFirst().get().getValue();
            }
        }
        return Arrays.stream(dimensions.split(";")[0].split("x"))
                .map(s -> Double.parseDouble(s.replaceAll("[^\\d.]", "")))
                .collect(Collectors.toList());
    }

    private AmazonSourcingProduct.BuyboxWinner mapBuyboxWinner(AmazonProductResponseDto.BuyboxWinner dto) {
        if (dto == null) {
            return null;
        }
        return AmazonSourcingProduct.BuyboxWinner.builder()
                .offerId(dto.getOfferId())
                .availability(mapAvailability(dto.getAvailability()))
                .unqualifiedBuyBox(dto.isUnqualifiedBuyBox())
                .price(mapPrice(dto.getPrice()))
                .shipping(mapShipping(dto.getShipping()))
                .build();
    }

    private AmazonSourcingProduct.Shipping mapShipping(AmazonProductResponseDto.Shipping dto) {
        if (dto == null) {
            return null;
        }
        return AmazonSourcingProduct.Shipping.builder()
                .raw(dto.getRaw())
                .value(dto.getValue())
                .build();
    }

    private AmazonSourcingProduct.Price mapPrice(AmazonProductResponseDto.Price dto) {
        if (dto == null) {
            return null;
        }
        return AmazonSourcingProduct.Price.builder()
                .symbol(dto.getSymbol())
                .value(dto.getValue())
                .currency(dto.getCurrency())
                .raw(dto.getRaw())
                .build();
    }

    private AmazonSourcingProduct.Availability mapAvailability(AmazonProductResponseDto.Availability dto) {
        if (dto == null) {
            return null;
        }
        return AmazonSourcingProduct.Availability.builder()
                .type(dto.getType())
                .raw(dto.getRaw())
                .build();
    }

    private Map<String, String> mapNameValue(List<AmazonProductResponseDto.NameValue> dto) {
        if (dto == null) {
            return null;
        }
        Map<String, String> nameValueMap = new LinkedHashTreeMap<>();
        for (AmazonProductResponseDto.NameValue nameValue : dto) {
            if (nameValue == null) {
                continue;
            }
            String transedName = translateEngToKor(nameValue.getName());
            String transedValue = translateEngToKor(nameValue.getValue());
            if (transedName == null || transedValue == null) {
                continue;
            }
            nameValueMap.put(transedName, transedValue);
        }
        return nameValueMap;
    }

    private AmazonSourcingProduct.Variant transVariant(AmazonProductResponseDto dto) throws OutOfStockException {
        List<Double> dimensions = transDimensions(dto);
        Double weight = transWeight(dto);
        return AmazonSourcingProduct.Variant.builder()
                .asin(dto.getProductId())
                .title(transTitle(dto))
                .buyboxWinner(transBuyboxWinner(dto))
                .link(dto.getLink())
                .weight(transWeight(dto))
                .dimensions(transDimensions(dto))
                .shippingWeight(transShippingWeight(dto))
                .mainImage(dto.getMainImage())
                .images(transImages(dto))
                .rating(dto.getRating())
                .attributes(transAttributes(dto))
                .specifications(transSpecifications(dto))
                .deliveryPrice(transDeliveryPrice(dimensions, weight))
                .availability(transAvailability(dto))
                .build();
    }

    private List<String> transImages(AmazonProductResponseDto dto) {
        return Optional.ofNullable(dto.getImages())
                .orElseGet(ArrayList::new)
                .stream()
                .map(AmazonProductResponseDto.Link::getLink)
                .collect(Collectors.toList());
    }

    private Double transShippingWeight(AmazonProductResponseDto dto) {
        String shippingWeightString = dto.getShippingWeight();
        if (shippingWeightString == null) {
            return null;
        }
        return Double.parseDouble(shippingWeightString.replaceAll("[^\\d.]", ""));
    }

    private Double transWeight(AmazonProductResponseDto dto) {
        String originalWeight = dto.getWeight();
        if (originalWeight != null) {
            return Double.parseDouble(originalWeight.replaceAll("[^\\d.]", ""));
        }

        List<AmazonProductResponseDto.NameValue> specifications = dto.getSpecifications();

        Double poundsWeightByName = getWeightByName(specifications, "pounds");
        if (poundsWeightByName != null) {
            return poundsWeightByName;
        }
        Double ouncesWeightByName = getWeightByName(specifications, "ounces");
        if (ouncesWeightByName != null) {
            return ounceToPound(ouncesWeightByName);
        }
        Double ouncesWeightByValue = getWeightByValue(specifications, "ounces");
        if (ouncesWeightByValue != null) {
            return ounceToPound(ouncesWeightByValue);
        }
        return getWeightByValue(specifications, "pounds");
    }

    private Double getWeightByName(List<AmazonProductResponseDto.NameValue> specifications, String weightUnit) {
        List<AmazonProductResponseDto.NameValue> weightEntry = specifications
                .stream()
                .filter(nv -> nv.getName().toLowerCase().contains("weight"))
                .collect(Collectors.toList());
        if (weightEntry.isEmpty()) {
            return null;
        }
        String originalWeight = weightEntry.stream()
                .findFirst()
                .get()
                .getValue();
        return originalWeight.contains(weightUnit) ? Double.parseDouble(originalWeight.replaceAll("[^\\d.]", "")) : null;
    }

    private Double getWeightByValue(List<AmazonProductResponseDto.NameValue> specifications, String weightUnit) {
        List<AmazonProductResponseDto.NameValue> weightEntry = specifications.stream()
                .filter(nv -> nv.getValue().toLowerCase().contains(weightUnit))
                .collect(Collectors.toList());
        if (weightEntry.isEmpty()) {
            return null;
        }
        String originalWeight = weightEntry.stream()
                .findFirst()
                .get()
                .getValue();
        String[] splitValue = originalWeight.split(";");
        return Double.parseDouble(splitValue[splitValue.length - 1].replaceAll("[^\\d.]", ""));
    }

    private Double ounceToPound(Double ounce) {
        return ounce * OUNCE_TO_POUND;
    }

    private String transTitle(AmazonProductResponseDto dto) {
        String title = dto.getTitle();
        if (title == null) {
            return null;
        }
        return translateEngToKor(title);
    }

    private Double transDeliveryPrice(List<Double> dimensions, Double weight) {
        try {
            return deliveryAgencyService.getDeliveryPrice(dimensions, weight);
        } catch (DeliveryPriceException exception) {
            log.error("Exception occurred during calculating delivery price", exception);
            return null;
        }
    }

    private int transAvailability(AmazonProductResponseDto dto) throws OutOfStockException {
        AmazonProductResponseDto.BuyboxWinner buyboxWinner = dto.getBuyboxWinner();
        AmazonProductResponseDto.Availability availability = buyboxWinner.getAvailability();
        String availabilityRaw = availability.getRaw();
        return createMaximumBuyCount(availabilityRaw);
    }

    private int createMaximumBuyCount(String availability) throws OutOfStockException {
        if (availability == null) {
            throw new OutOfStockException();
        }

        String availabilityLowerCase = availability.toLowerCase();
        if (!availabilityLowerCase.contains("in stock")) {
            throw new OutOfStockException();
        }

        String stockCount = availabilityLowerCase.replaceAll("[\\D]", "");
        if (stockCount.equals("")) {
            return MAXIMUM_BUY_COUNT;
        }

        int stockCountInt = Integer.parseInt(stockCount);
        if (stockCountInt < 5) {
            throw new OutOfStockException();
        }
        return stockCountInt;
    }

    private AmazonSourcingProduct.BuyboxWinner transBuyboxWinner(AmazonProductResponseDto dto) {
        AmazonProductResponseDto.BuyboxWinner buyboxWinner = dto.getBuyboxWinner();
        if (buyboxWinner == null) {
            return null;
        }
        return mapBuyboxWinner(buyboxWinner);
    }

    private String translateEngToKor(String text) {
        if (text == null) {
            return null;
        }
        return papagoTextTranslator.translateText(text, TRANSLATE_FROM, TRANSLATE_TO);
    }

    private List<String> translateEngToKor(List<String> source) {
        if (source == null) {
            return null;
        }
        List<String> target = new ArrayList<>();
        for (String text : source) {
            target.add(papagoTextTranslator.translateText(text, TRANSLATE_FROM, TRANSLATE_TO));
        }
        return target;
    }

}
