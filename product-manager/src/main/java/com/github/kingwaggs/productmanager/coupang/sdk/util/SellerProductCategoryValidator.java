package com.github.kingwaggs.productmanager.coupang.sdk.util;

import com.github.kingwaggs.productmanager.coupang.sdk.config.Configuration;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.*;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApiClient;

import java.util.*;

public class SellerProductCategoryValidator {
    private static CoupangMarketPlaceApiClient apiClient = Configuration.getDefaultApiClient();
    private static Map<Long, OpenApiResultOfOCategoryMeta> categorymetaDataMap = new HashMap();

    public SellerProductCategoryValidator() {
    }

    public static boolean validateSellerProduct(String vendorId, Long displayCategoryId, String secretKey, String accessKey, OSellerProduct oSellerProduct, ResourceBundle resourceBundle) {
        oSellerProduct.setContributorType("API_Seller_SDKJavaV1");
        boolean result = true;

        try {
            OpenApiResultOfOCategoryMeta categoryMeta;
            if (categorymetaDataMap.containsKey(displayCategoryId)) {
                categoryMeta = (OpenApiResultOfOCategoryMeta)categorymetaDataMap.get(displayCategoryId);
            } else {
                CoupangMarketPlaceApi apiInstance = new CoupangMarketPlaceApi(secretKey, accessKey);
                categoryMeta = apiInstance.getCategoryRelatedMetaByDisplayCategoryCode(vendorId, displayCategoryId.toString());
                categorymetaDataMap.put(displayCategoryId, categoryMeta);
            }

            if (oSellerProduct.getItems().size() == 0) {
                throw new ApiException(String.format(resourceBundle.getString("error.message.item.not.specified"), oSellerProduct.getSellerProductName()));
            }

            Iterator var23 = oSellerProduct.getItems().iterator();

            while(var23.hasNext()) {
                OSellerProductItem item = (OSellerProductItem)var23.next();
                if (item.getNotices().size() == 0) {
                    throw new ApiException(String.format(resourceBundle.getString("error.message.item.notice.not.specified"), item.getItemName()));
                }
            }

            int numberOfAttributes = ((OSellerProductItem)oSellerProduct.getItems().get(0)).getAttributes().size();
            Iterator var25 = oSellerProduct.getItems().iterator();

            while(var25.hasNext()) {
                OSellerProductItem item = (OSellerProductItem)var25.next();
                if (item.getAttributes().size() != numberOfAttributes) {
                    throw new ApiException(String.format(resourceBundle.getString("error.message.items.have.different.number.of.attributes"), oSellerProduct.getSellerProductName()));
                }
            }

            List<String> requiredAttributes = new ArrayList();
            Iterator var27 = categoryMeta.getData().getAttributes().iterator();

            while(var27.hasNext()) {
                OAttributeType item = (OAttributeType)var27.next();
                if ("MANDATORY".equals(item.getRequired())) {
                    requiredAttributes.add(item.getAttributeTypeName());
                }
            }

            List<String> anyItemMissingRequiredAttributes = new ArrayList();
            Iterator var29 = oSellerProduct.getItems().iterator();

            while(var29.hasNext()) {
                OSellerProductItem item = (OSellerProductItem)var29.next();
                List<String> itemAttrNames = new ArrayList();
                Iterator var14 = item.getAttributes().iterator();

                while(var14.hasNext()) {
                    OSellerProductItemAttribute itemAttr = (OSellerProductItemAttribute)var14.next();
                    itemAttrNames.add(itemAttr.getAttributeTypeName());
                }

                var14 = requiredAttributes.iterator();

                while(var14.hasNext()) {
                    String reqAttr = (String)var14.next();
                    if (!itemAttrNames.contains(reqAttr)) {
                        anyItemMissingRequiredAttributes.add(reqAttr);
                    }
                }
            }

            if (anyItemMissingRequiredAttributes.size() > 0) {
                throw new ApiException(String.format(resourceBundle.getString("error.message.item.missing.required.attributes"), anyItemMissingRequiredAttributes.toArray(), requiredAttributes));
            }

            OSellerProductItem itemNeedDocuments = null;
            Iterator var31 = oSellerProduct.getItems().iterator();

            OSellerProductItem item;
            label130: {
                do {
                    if (!var31.hasNext()) {
                        break label130;
                    }

                    item = (OSellerProductItem)var31.next();
                } while(item.getParallelImported() != OSellerProductItem.ParallelImportedEnum.PARALLEL_IMPORTED && item.getOverseasPurchased() != OSellerProductItem.OverseasPurchasedEnum.OVERSEAS_PURCHASED);

                itemNeedDocuments = item;
            }

            if (itemNeedDocuments != null && oSellerProduct.getRequiredDocuments().size() == 0) {
                throw new ApiException(String.format(resourceBundle.getString("error.message.item.missing.required.document"), oSellerProduct.getSellerProductName()));
            }

            var31 = oSellerProduct.getItems().iterator();

            label120:
            while(var31.hasNext()) {
                item = (OSellerProductItem)var31.next();
                Set<String> noticeCategorySet = new HashSet();
                List<String> itemNotices = new ArrayList();
                Iterator var16 = item.getNotices().iterator();

                while(var16.hasNext()) {
                    OSellerProductItemNotice detail = (OSellerProductItemNotice)var16.next();
                    noticeCategorySet.add(detail.getNoticeCategoryName());
                    itemNotices.add(detail.getNoticeCategoryDetailName());
                }

                if (noticeCategorySet.size() > 1) {
                    throw new ApiException(String.format(resourceBundle.getString("error.message.item.notice.one.category")));
                }

                String selectedNoticeCategoryName = (String)noticeCategorySet.iterator().next();
                List<String> requiredCategoryTemplates = new ArrayList();
                Iterator var18 = categoryMeta.getData().getNoticeCategories().iterator();

                while(true) {
                    ONoticeCategory noticeCategory;
                    do {
                        if (!var18.hasNext()) {
                            var18 = requiredCategoryTemplates.iterator();

                            while(var18.hasNext()) {
                                String requiredNoticeDetail = (String)var18.next();
                                if (!itemNotices.contains(requiredNoticeDetail)) {
                                    throw new ApiException(String.format(resourceBundle.getString("error.message.item.not.conform.required.notice.template"), item.getItemName()));
                                }
                            }
                            continue label120;
                        }

                        noticeCategory = (ONoticeCategory)var18.next();
                    } while(!selectedNoticeCategoryName.equals(noticeCategory.getNoticeCategoryName()));

                    Iterator var20 = noticeCategory.getNoticeCategoryDetailNames().iterator();

                    while(var20.hasNext()) {
                        ONoticeCategoryTemplate categoryDetail = (ONoticeCategoryTemplate)var20.next();
                        if ("MANDATORY".equals(categoryDetail.getRequired())) {
                            requiredCategoryTemplates.add(categoryDetail.getNoticeCategoryDetailName());
                        }
                    }
                }
            }
        } catch (ApiException var22) {
            result = false;
            System.err.println(resourceBundle.getString("error.message.validation") + ", " + var22.getMessage());
        }

        return result;
    }
}
