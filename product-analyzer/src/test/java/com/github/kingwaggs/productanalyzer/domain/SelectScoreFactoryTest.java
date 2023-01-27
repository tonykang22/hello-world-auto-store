package com.github.kingwaggs.productanalyzer.domain;

import com.github.kingwaggs.productanalyzer.domain.dto.ItemScoutIndicatorDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SelectScoreFactoryTest {
    private final SelectScoreFactory selectScoreFactory = new SelectScoreFactory();

    @Test
    @DisplayName("상,하위 10% 안에 드는 지표가 하나라도 있을시 필터링 - 모두 필터링 된 경우")
    void filtered_all() {
        //Arrange
        ItemScoutIndicatorDto topIOCIndicator = createItemScoutIndicatorsDto(5, 4, 5, 5, 2, 5);
        ItemScoutIndicatorDto topPOPPIndicator = createItemScoutIndicatorsDto(4, 5, 4, 4, 3, 5);
        ItemScoutIndicatorDto topPOOPIndicator = createItemScoutIndicatorsDto(3, 3, 1, 3, 4, 5);
        ItemScoutIndicatorDto topPOAPIndicator = createItemScoutIndicatorsDto(2, 2, 2, 1, 5, 5);
        ItemScoutIndicatorDto topPRW1YIndicator = createItemScoutIndicatorsDto(1, 1, 3, 2, 1, 5);
        List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = List.of(topIOCIndicator, topPOPPIndicator, topPOOPIndicator, topPOAPIndicator, topPRW1YIndicator);

        //Act
        List<SelectScore> actual = selectScoreFactory.create(itemScoutIndicatorDtoList);

        //Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("상,하위 10% 안에 드는 지표가 하나라도 있을시 필터링 - 일부만 필터링 된 경우")
    void filtered_some() {
        //Arrange
        // 반비례지표(경쟁강도,묶음상품비율) 모두 상위 10%에 속함 - 필터링 대상
        ItemScoutIndicatorDto indicator1 = createItemScoutIndicatorsDto(5, 5, 5, 5, 2, 2);
        // 모든 지표가 상,하위 10%에 속하지 않음 - 필터링 대상 아님
        ItemScoutIndicatorDto indicator2 = createItemScoutIndicatorsDto(4, 4, 4, 4, 3, 4);
        // 비례지표(해외상품비율,실거래비율) 모두 하위 10%에 속함 - 필터링 대상
        ItemScoutIndicatorDto indicator3 = createItemScoutIndicatorsDto(3, 3, 1, 1, 4, 3);
        // 모든 지표가 상,하위 10%에 속하지 않음 - 필터링 대상 아님
        ItemScoutIndicatorDto indicator4 = createItemScoutIndicatorsDto(2, 2, 2, 3, 5, 5);
        // 비례지표(1년 내 게시 비율)가 하위 10%에 속함 - 필터링 대상
        ItemScoutIndicatorDto indicator5 = createItemScoutIndicatorsDto(1, 1, 3, 2, 1, 1);
        List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = List.of(indicator1,indicator2,indicator3,indicator4,indicator5);

        //Act
        List<SelectScore> actual = selectScoreFactory.create(itemScoutIndicatorDtoList);

        //Assert
        int expected = 2;
        assertEquals(expected, actual.size());
    }

    @Test
    @DisplayName("계산 결과 테스트")
    void success_default() {
        //Arrange
        // 모든 지표가 상,하위 10%에 속함 - 필터링 대상
        ItemScoutIndicatorDto filteredIndicator = createItemScoutIndicatorsDto("filtered", 3, 3, 0, 0, 0, 0);
        // Min-Max 스케일링(최댓값=1,최솟값=0) 후 각 지표 값 -> (1,0,1,0,1,0) -> selectScore = 각 지표별로 가중치와 곱한 결과들의 합
        String indicator1 = "indicator1";
        ItemScoutIndicatorDto unfilteredIndicator1 = createItemScoutIndicatorsDto(indicator1, 2, 1, 2, 1, 2, 1);
        // Min-Max 스케일링(최댓값=1,최솟값=0) 후 각 지표 값 -> (0,1,0,1,0,1) -> selectScore = 각 지표별로 가중치와 곱한 결과들의 합
        String indicator2 = "indicator2";
        ItemScoutIndicatorDto unfilteredIndicator2 = createItemScoutIndicatorsDto(indicator2, 1, 2, 1, 2, 1, 2);
        List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = List.of(filteredIndicator, unfilteredIndicator1, unfilteredIndicator2);

        double expectedSelectScore1 = 1 * SelectScoreFactory.Weight.PROPORTION_OF_PACKAGE_PRODUCTS.getValue() + 1 * SelectScoreFactory.Weight.PROPORTION_OF_OVERSEAS_PRODUCTS.getValue() + 1 * SelectScoreFactory.Weight.POSTING_RATE_WITHIN_1_YEAR.getValue();
        double expectedSelectScore2 = 1 * SelectScoreFactory.Weight.INTENSITY_OF_COMPETITION.getValue() + 1 * SelectScoreFactory.Weight.PROPORTION_OF_ACTUAL_PURCHASE.getValue() + 1 * SelectScoreFactory.Weight.AVERAGE_PRICE.getValue();

        //Act
        List<SelectScore> actual = selectScoreFactory.create(itemScoutIndicatorDtoList);
        double actualSelectScore1 = actual.stream().filter(ss -> ss.getSearchWord().equals(indicator1)).findFirst().orElseThrow().getScore();
        double actualSelectScore2 = actual.stream().filter(ss -> ss.getSearchWord().equals(indicator2)).findFirst().orElseThrow().getScore();

        //Assert
        assertEquals(expectedSelectScore1, actualSelectScore1);
        assertEquals(expectedSelectScore2, actualSelectScore2);
    }

    @Test
    void pass_null() {
        //Arrange
        List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = null;

        //Act
        List<SelectScore> actual = selectScoreFactory.create(itemScoutIndicatorDtoList);

        //Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    void pass_empty_list() {
        //Arrange
        List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = Collections.emptyList();

        //Act
        List<SelectScore> actual = selectScoreFactory.create(itemScoutIndicatorDtoList);

        //Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    void pass_empty_elements() {
        //Arrange
        ItemScoutIndicatorDto mockItemScoutIndicatorDto1 = mock(ItemScoutIndicatorDto.class);
        ItemScoutIndicatorDto mockItemScoutIndicatorDto2 = mock(ItemScoutIndicatorDto.class);
        List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = List.of(mockItemScoutIndicatorDto1, mockItemScoutIndicatorDto2);

        //Act
        List<SelectScore> actual = selectScoreFactory.create(itemScoutIndicatorDtoList);

        //Assert
        assertTrue(actual.isEmpty());
    }

    private ItemScoutIndicatorDto createItemScoutIndicatorsDto(double intensityOfCompetition, double proportionOfPackageProducts, double proportionOfOverseasProducts, double proportionOfActualPurchase, double postingRateWithin1Year, double averagePrice) {
        return ItemScoutIndicatorDto.builder()
                .intensityOfCompetition(intensityOfCompetition)
                .proportionOfPackageProducts(proportionOfPackageProducts)
                .proportionOfOverseasProducts(proportionOfOverseasProducts)
                .proportionOfActualPurchase(proportionOfActualPurchase)
                .postingRateWithin1Year(postingRateWithin1Year)
                .averagePrice(averagePrice)
                .build();
    }

    private ItemScoutIndicatorDto createItemScoutIndicatorsDto(String searchTerm, double intensityOfCompetition, double proportionOfPackageProducts, double proportionOfOverseasProducts, double proportionOfActualPurchase, double postingRateWithin1Year, double averagePrice) {
        return ItemScoutIndicatorDto.builder()
                .searchTerm(searchTerm)
                .intensityOfCompetition(intensityOfCompetition)
                .proportionOfPackageProducts(proportionOfPackageProducts)
                .proportionOfOverseasProducts(proportionOfOverseasProducts)
                .proportionOfActualPurchase(proportionOfActualPurchase)
                .postingRateWithin1Year(postingRateWithin1Year)
                .averagePrice(averagePrice)
                .build();
    }
}