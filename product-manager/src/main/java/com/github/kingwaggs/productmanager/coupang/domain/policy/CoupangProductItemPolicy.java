package com.github.kingwaggs.productmanager.coupang.domain.policy;

import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.OSellerProductItem;
import lombok.Data;

/**
 * Fixed policy of Coupang product properties
 *
 * @author waggs (github.com/kingwaggs)
 */

@Data
public class CoupangProductItemPolicy {
    // 판매가능수량, 최대 99999
    // HWAS 정책은 5개가 최대
    public static final Integer MAXIMUM_BUY_COUNT = 5;

    // 1인당 최대 구매 가능한 수량, 제한이 없을 경우 ‘0’을 입력
    public static final Integer MAXIMUM_BUY_FOR_PERSON = 0;

    // 최대 구매 수량 기간, 1인당 해당 상품을 구매할 수 있는 주기 설정, 제한이 없을 경우 ‘1’을 입력
    public static final Integer MAXIMUM_BUY_FOR_PERSON_PERIOD = 1;

    // 기준출고일(일), 주문일(D-Day) 이후 배송을 위한 출고예정일자를 '일' 위로 입력.
    // 주문일(D-Day) 이후 최대 20일 이내 출고로 설정
    public static final Integer OUTBOUND_SHIPPING_TIME_DAY = 14;

    // 단위수량, 상품에 포함된 수량을 입력하면 (판매가격 ÷ 단위수량) 로 계산하여 (1개당 가격 #,000원) 으로 노출
    // 개당가격이 필요하지 않은 상품은 '0'을 입력
    public static final Integer UNIT_COUNT = 0;

    // 19세이상 여부, EVERYONE
    public static final OSellerProductItem.AdultOnlyEnum ADULT_ONLY = OSellerProductItem.AdultOnlyEnum.EVERYONE;

    // 과세 여부, TAX FREE 아닌 이상 무조건 TAX
    public static final OSellerProductItem.TaxTypeEnum TAX_TYPE = OSellerProductItem.TaxTypeEnum.TAX;

    // 병행 수입 여부, 병행수입 아님
    public static final OSellerProductItem.ParallelImportedEnum PARALLEL_IMPORTED = OSellerProductItem.ParallelImportedEnum.NOT_PARALLEL_IMPORTED;

    // 해외 구매대행 여부, 구매대행 맞음
    public static final OSellerProductItem.OverseasPurchasedEnum OVERSEAS_PURCHASED = OSellerProductItem.OverseasPurchasedEnum.OVERSEAS_PURCHASED;

    public static final Boolean PCC_NEEDED = true;
}
