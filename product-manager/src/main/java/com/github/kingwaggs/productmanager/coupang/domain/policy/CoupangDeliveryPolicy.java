package com.github.kingwaggs.productmanager.coupang.domain.policy;

import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.OSellerProduct;

/**
 * Fixed policy of Coupang delivery properties
 * @see <a href="https://github.com/kingwaggs/hello-world-auto-store/issues/26">[product-manager] 상품 배송 정책</a>
 * @author waggs (github.com/kingwaggs)
 */

public class CoupangDeliveryPolicy {

    // 배송 방법 = 구매대행
    public static final OSellerProduct.DeliveryMethodEnum DELIVERY_METHOD = OSellerProduct.DeliveryMethodEnum.AGENT_BUY;

    // 계약 택배사 코드(https://developers.coupangcorp.com/hc/ko/articles/360034156033)
    public static final String DELIVERY_COMPANY_CODE = "CJGLS";

    // 배송비 종류 = 무료배송
    public static final OSellerProduct.DeliveryChargeTypeEnum DELIVERY_CHARGE_TYPE = OSellerProduct.DeliveryChargeTypeEnum.FREE;

    // 기본 배송비 = 무료배송시 0 으로 설정
    public static final Double DELIVERY_CHARGE = (double) 0;

    // 무료배송을 위한 조건 금액 = 무료배송시 0 으로 설정
    public static final Double FREE_SHIP_OVER_AMOUNT = (double) 0;

    // 도서 산간 배송 여부 = 도서 산간 배송 안함
    public static final OSellerProduct.RemoteAreaDeliverableEnum REMOTE_AREA_DELIVERABLE = OSellerProduct.RemoteAreaDeliverableEnum.Y;

    // 묶음 배송 여부 = 묶음 배송 안함
    public static final OSellerProduct.UnionDeliveryTypeEnum UNION_DELIVERY_TYPE = OSellerProduct.UnionDeliveryTypeEnum.NOT_UNION_DELIVERY;

    // 초도반품배송비 (deliveryChargeOnReturn) : 무료배송 시, 판매자가 지불한 금액에 대해 고객에게 청구하는 배송비
    public static final Double DELIVERY_CHARGE_ON_RETURN = 30_000.0;

    // 반품배송비 (returnCharge) : 반품받을 때 지불하는 편도 비용
    public static final Double RETURN_CHARGE = 40_000.0;
}
