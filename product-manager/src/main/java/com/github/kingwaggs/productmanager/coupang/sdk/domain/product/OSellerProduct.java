package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class OSellerProduct {

    @SerializedName("vendorUserId")
    private String vendorUserId = null;
    @SerializedName("vendorId")
    private String vendorId = null;
    @SerializedName("displayProductName")
    private String displayProductName = null;
    @SerializedName("generalProductName")
    private String generalProductName = null;
    @SerializedName("productGroup")
    private String productGroup = null;
    @SerializedName("unionDeliveryType")
    private OSellerProduct.UnionDeliveryTypeEnum unionDeliveryType = null;
    @SerializedName("statusName")
    private String statusName = null;
    @SerializedName("sellerProductName")
    private String sellerProductName = null;
    @SerializedName("sellerProductId")
    private Long sellerProductId = null;
    @SerializedName("saleStartedAt")
    private String saleStartedAt = null;
    @SerializedName("saleEndedAt")
    private String saleEndedAt = null;
    @SerializedName("returnZipCode")
    private String returnZipCode = null;
    @SerializedName("returnChargeVendor")
    private OSellerProduct.ReturnChargeVendorEnum returnChargeVendor = null;
    @SerializedName("returnChargeName")
    private String returnChargeName = null;
    @SerializedName("returnCharge")
    private Double returnCharge = null;
    @SerializedName("returnCenterCode")
    private String returnCenterCode = null;
    @SerializedName("returnAddressDetail")
    private String returnAddressDetail = null;
    @SerializedName("returnAddress")
    private String returnAddress = null;
    @SerializedName("requiredDocuments")
    private List<OSellerProductRequiredDocument> requiredDocuments = new ArrayList();
    @SerializedName("requested")
    private Boolean requested = null;
    @SerializedName("productId")
    private Long productId = null;
    @SerializedName("outboundShippingPlaceCode")
    private Long outboundShippingPlaceCode = null;
    @SerializedName("mdName")
    private String mdName = null;
    @SerializedName("mdId")
    private String mdId = null;
    @SerializedName("manufacture")
    private String manufacture = null;
    @SerializedName("extraInfoMessage")
    private String extraInfoMessage = null;
    @SerializedName("items")
    private List<OSellerProductItem> items = new ArrayList();
    @SerializedName("freeShipOverAmount")
    private Double freeShipOverAmount = null;
    @SerializedName("exchangeType")
    private String exchangeType = null;
    @SerializedName("displayCategoryCode")
    private Long displayCategoryCode = null;
    @SerializedName("deliverySurcharge")
    private Double deliverySurcharge = null;
    @SerializedName("deliveryMethod")
    private OSellerProduct.DeliveryMethodEnum deliveryMethod = null;
    @SerializedName("deliveryCompanyCode")
    private String deliveryCompanyCode = null;
    @SerializedName("deliveryChargeType")
    private OSellerProduct.DeliveryChargeTypeEnum deliveryChargeType = null;
    @SerializedName("deliveryChargeOnReturn")
    private Double deliveryChargeOnReturn = null;
    @SerializedName("deliveryCharge")
    private Double deliveryCharge = null;
    @SerializedName("contributorType")
    private String contributorType = null;
    @SerializedName("companyContactNumber")
    private String companyContactNumber = null;
    @SerializedName("categoryId")
    private Long categoryId = null;
    @SerializedName("bundlePackingDelivery")
    private Integer bundlePackingDelivery = null;
    @SerializedName("brand")
    private String brand = null;
    @SerializedName("afterServiceInformation")
    private String afterServiceInformation = null;
    @SerializedName("afterServiceContactNumber")
    private String afterServiceContactNumber = null;
    @SerializedName("remoteAreaDeliverable")
    private OSellerProduct.RemoteAreaDeliverableEnum remoteAreaDeliverable = null;

    public OSellerProduct() {
    }

    public OSellerProduct vendorUserId(String vendorUserId) {
        this.vendorUserId = vendorUserId;
        return this;
    }

    public String getVendorUserId() {
        return this.vendorUserId;
    }

    public void setVendorUserId(String vendorUserId) {
        this.vendorUserId = vendorUserId;
    }

    public OSellerProduct vendorId(String vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public OSellerProduct displayProductName(String displayProductName) {
        this.displayProductName = displayProductName;
        return this;
    }

    public String getDisplayProductName() {
        return this.displayProductName;
    }

    public void setDisplayProductName(String displayProductName) {
        this.displayProductName = displayProductName;
    }

    public OSellerProduct generalProductName(String generalProductName) {
        this.generalProductName = generalProductName;
        return this;
    }

    public String getGeneralProductName() {
        return this.generalProductName;
    }

    public void setGeneralProductName(String generalProductName) {
        this.generalProductName = generalProductName;
    }

    public OSellerProduct productGroup(String productGroup) {
        this.productGroup = productGroup;
        return this;
    }

    public String getProductGroup() {
        return this.productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public OSellerProduct unionDeliveryType(OSellerProduct.UnionDeliveryTypeEnum unionDeliveryType) {
        this.unionDeliveryType = unionDeliveryType;
        return this;
    }

    public OSellerProduct.UnionDeliveryTypeEnum getUnionDeliveryType() {
        return this.unionDeliveryType;
    }

    public void setUnionDeliveryType(OSellerProduct.UnionDeliveryTypeEnum unionDeliveryType) {
        this.unionDeliveryType = unionDeliveryType;
    }

    public OSellerProduct statusName(String statusName) {
        this.statusName = statusName;
        return this;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public OSellerProduct sellerProductName(String sellerProductName) {
        this.sellerProductName = sellerProductName;
        return this;
    }

    public String getSellerProductName() {
        return this.sellerProductName;
    }

    public void setSellerProductName(String sellerProductName) {
        this.sellerProductName = sellerProductName;
    }

    public OSellerProduct sellerProductId(Long sellerProductId) {
        this.sellerProductId = sellerProductId;
        return this;
    }

    public Long getSellerProductId() {
        return this.sellerProductId;
    }

    public void setSellerProductId(Long sellerProductId) {
        this.sellerProductId = sellerProductId;
    }

    public OSellerProduct saleStartedAt(String saleStartedAt) {
        this.saleStartedAt = saleStartedAt;
        return this;
    }

    public String getSaleStartedAt() {
        return this.saleStartedAt;
    }

    public void setSaleStartedAt(String saleStartedAt) {
        this.saleStartedAt = saleStartedAt;
    }

    public OSellerProduct saleEndedAt(String saleEndedAt) {
        this.saleEndedAt = saleEndedAt;
        return this;
    }

    public String getSaleEndedAt() {
        return this.saleEndedAt;
    }

    public void setSaleEndedAt(String saleEndedAt) {
        this.saleEndedAt = saleEndedAt;
    }

    public OSellerProduct returnZipCode(String returnZipCode) {
        this.returnZipCode = returnZipCode;
        return this;
    }

    public String getReturnZipCode() {
        return this.returnZipCode;
    }

    public void setReturnZipCode(String returnZipCode) {
        this.returnZipCode = returnZipCode;
    }

    public OSellerProduct returnChargeVendor(OSellerProduct.ReturnChargeVendorEnum returnChargeVendor) {
        this.returnChargeVendor = returnChargeVendor;
        return this;
    }

    public OSellerProduct.ReturnChargeVendorEnum getReturnChargeVendor() {
        return this.returnChargeVendor;
    }

    public void setReturnChargeVendor(OSellerProduct.ReturnChargeVendorEnum returnChargeVendor) {
        this.returnChargeVendor = returnChargeVendor;
    }

    public OSellerProduct returnChargeName(String returnChargeName) {
        this.returnChargeName = returnChargeName;
        return this;
    }

    public String getReturnChargeName() {
        return this.returnChargeName;
    }

    public void setReturnChargeName(String returnChargeName) {
        this.returnChargeName = returnChargeName;
    }

    public OSellerProduct returnCharge(Double returnCharge) {
        this.returnCharge = returnCharge;
        return this;
    }

    public Double getReturnCharge() {
        return this.returnCharge;
    }

    public void setReturnCharge(Double returnCharge) {
        this.returnCharge = returnCharge;
    }

    public OSellerProduct returnCenterCode(String returnCenterCode) {
        this.returnCenterCode = returnCenterCode;
        return this;
    }

    public String getReturnCenterCode() {
        return this.returnCenterCode;
    }

    public void setReturnCenterCode(String returnCenterCode) {
        this.returnCenterCode = returnCenterCode;
    }

    public OSellerProduct returnAddressDetail(String returnAddressDetail) {
        this.returnAddressDetail = returnAddressDetail;
        return this;
    }

    public String getReturnAddressDetail() {
        return this.returnAddressDetail;
    }

    public void setReturnAddressDetail(String returnAddressDetail) {
        this.returnAddressDetail = returnAddressDetail;
    }

    public OSellerProduct returnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
        return this;
    }

    public String getReturnAddress() {
        return this.returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public OSellerProduct requiredDocuments(List<OSellerProductRequiredDocument> requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
        return this;
    }

    public OSellerProduct addRequiredDocumentsItem(OSellerProductRequiredDocument requiredDocumentsItem) {
        this.requiredDocuments.add(requiredDocumentsItem);
        return this;
    }

    public List<OSellerProductRequiredDocument> getRequiredDocuments() {
        return this.requiredDocuments;
    }

    public void setRequiredDocuments(List<OSellerProductRequiredDocument> requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
    }

    public OSellerProduct requested(Boolean requested) {
        this.requested = requested;
        return this;
    }

    public Boolean getRequested() {
        return this.requested;
    }

    public void setRequested(Boolean requested) {
        this.requested = requested;
    }

    public OSellerProduct productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public OSellerProduct outboundShippingPlaceCode(Long outboundShippingPlaceCode) {
        this.outboundShippingPlaceCode = outboundShippingPlaceCode;
        return this;
    }

    public Long getOutboundShippingPlaceCode() {
        return this.outboundShippingPlaceCode;
    }

    public void setOutboundShippingPlaceCode(Long outboundShippingPlaceCode) {
        this.outboundShippingPlaceCode = outboundShippingPlaceCode;
    }

    public OSellerProduct mdName(String mdName) {
        this.mdName = mdName;
        return this;
    }

    public String getMdName() {
        return this.mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
    }

    public OSellerProduct mdId(String mdId) {
        this.mdId = mdId;
        return this;
    }

    public String getMdId() {
        return this.mdId;
    }

    public void setMdId(String mdId) {
        this.mdId = mdId;
    }

    public OSellerProduct manufacture(String manufacture) {
        this.manufacture = manufacture;
        return this;
    }

    public String getManufacture() {
        return this.manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public OSellerProduct extraInfoMessage(String extraInfoMessage) {
        this.extraInfoMessage = extraInfoMessage;
        return this;
    }

    public String getExtraInfoMessage() {
        return this.extraInfoMessage;
    }

    public void setExtraInfoMessage(String extraInfoMessage) {
        this.extraInfoMessage = extraInfoMessage;
    }

    public OSellerProduct items(List<OSellerProductItem> items) {
        this.items = items;
        return this;
    }

    public OSellerProduct addItemsItem(OSellerProductItem itemsItem) {
        this.items.add(itemsItem);
        return this;
    }

    public List<OSellerProductItem> getItems() {
        return this.items;
    }

    public void setItems(List<OSellerProductItem> items) {
        this.items = items;
    }

    public OSellerProduct freeShipOverAmount(Double freeShipOverAmount) {
        this.freeShipOverAmount = freeShipOverAmount;
        return this;
    }

    public Double getFreeShipOverAmount() {
        return this.freeShipOverAmount;
    }

    public void setFreeShipOverAmount(Double freeShipOverAmount) {
        this.freeShipOverAmount = freeShipOverAmount;
    }

    public OSellerProduct exchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
        return this;
    }

    public String getExchangeType() {
        return this.exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public OSellerProduct displayCategoryCode(Long displayCategoryCode) {
        this.displayCategoryCode = displayCategoryCode;
        return this;
    }

    public Long getDisplayCategoryCode() {
        return this.displayCategoryCode;
    }

    public void setDisplayCategoryCode(Long displayCategoryCode) {
        this.displayCategoryCode = displayCategoryCode;
    }

    public OSellerProduct deliverySurcharge(Double deliverySurcharge) {
        this.deliverySurcharge = deliverySurcharge;
        return this;
    }

    public Double getDeliverySurcharge() {
        return this.deliverySurcharge;
    }

    public void setDeliverySurcharge(Double deliverySurcharge) {
        this.deliverySurcharge = deliverySurcharge;
    }

    public OSellerProduct deliveryMethod(OSellerProduct.DeliveryMethodEnum deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
        return this;
    }

    public OSellerProduct.DeliveryMethodEnum getDeliveryMethod() {
        return this.deliveryMethod;
    }

    public void setDeliveryMethod(OSellerProduct.DeliveryMethodEnum deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public OSellerProduct deliveryCompanyCode(String deliveryCompanyCode) {
        this.deliveryCompanyCode = deliveryCompanyCode;
        return this;
    }

    public String getDeliveryCompanyCode() {
        return this.deliveryCompanyCode;
    }

    public void setDeliveryCompanyCode(String deliveryCompanyCode) {
        this.deliveryCompanyCode = deliveryCompanyCode;
    }

    public OSellerProduct deliveryChargeType(OSellerProduct.DeliveryChargeTypeEnum deliveryChargeType) {
        this.deliveryChargeType = deliveryChargeType;
        return this;
    }

    public OSellerProduct.DeliveryChargeTypeEnum getDeliveryChargeType() {
        return this.deliveryChargeType;
    }

    public void setDeliveryChargeType(OSellerProduct.DeliveryChargeTypeEnum deliveryChargeType) {
        this.deliveryChargeType = deliveryChargeType;
    }

    public OSellerProduct deliveryChargeOnReturn(Double deliveryChargeOnReturn) {
        this.deliveryChargeOnReturn = deliveryChargeOnReturn;
        return this;
    }

    public Double getDeliveryChargeOnReturn() {
        return this.deliveryChargeOnReturn;
    }

    public void setDeliveryChargeOnReturn(Double deliveryChargeOnReturn) {
        this.deliveryChargeOnReturn = deliveryChargeOnReturn;
    }

    public OSellerProduct deliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
        return this;
    }

    public Double getDeliveryCharge() {
        return this.deliveryCharge;
    }

    public void setDeliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public OSellerProduct contributorType(String contributorType) {
        this.contributorType = contributorType;
        return this;
    }

    public String getContributorType() {
        return this.contributorType;
    }

    public void setContributorType(String contributorType) {
        this.contributorType = contributorType;
    }

    public OSellerProduct companyContactNumber(String companyContactNumber) {
        this.companyContactNumber = companyContactNumber;
        return this;
    }

    public String getCompanyContactNumber() {
        return this.companyContactNumber;
    }

    public void setCompanyContactNumber(String companyContactNumber) {
        this.companyContactNumber = companyContactNumber;
    }

    public OSellerProduct categoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public OSellerProduct bundlePackingDelivery(Integer bundlePackingDelivery) {
        this.bundlePackingDelivery = bundlePackingDelivery;
        return this;
    }

    public Integer getBundlePackingDelivery() {
        return this.bundlePackingDelivery;
    }

    public void setBundlePackingDelivery(Integer bundlePackingDelivery) {
        this.bundlePackingDelivery = bundlePackingDelivery;
    }

    public OSellerProduct brand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public OSellerProduct afterServiceInformation(String afterServiceInformation) {
        this.afterServiceInformation = afterServiceInformation;
        return this;
    }

    public String getAfterServiceInformation() {
        return this.afterServiceInformation;
    }

    public void setAfterServiceInformation(String afterServiceInformation) {
        this.afterServiceInformation = afterServiceInformation;
    }

    public OSellerProduct afterServiceContactNumber(String afterServiceContactNumber) {
        this.afterServiceContactNumber = afterServiceContactNumber;
        return this;
    }

    public String getAfterServiceContactNumber() {
        return this.afterServiceContactNumber;
    }

    public void setAfterServiceContactNumber(String afterServiceContactNumber) {
        this.afterServiceContactNumber = afterServiceContactNumber;
    }

    public OSellerProduct remoteAreaDeliverable(OSellerProduct.RemoteAreaDeliverableEnum remoteAreaDeliverable) {
        this.remoteAreaDeliverable = remoteAreaDeliverable;
        return this;
    }

    public OSellerProduct.RemoteAreaDeliverableEnum getRemoteAreaDeliverable() {
        return this.remoteAreaDeliverable;
    }

    public void setRemoteAreaDeliverable(OSellerProduct.RemoteAreaDeliverableEnum remoteAreaDeliverable) {
        this.remoteAreaDeliverable = remoteAreaDeliverable;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OSellerProduct osellerProduct = (OSellerProduct)o;
            return Objects.equals(this.vendorUserId, osellerProduct.vendorUserId) && Objects.equals(this.vendorId, osellerProduct.vendorId) && Objects.equals(this.displayProductName, osellerProduct.displayProductName) && Objects.equals(this.generalProductName, osellerProduct.generalProductName) && Objects.equals(this.productGroup, osellerProduct.productGroup) && Objects.equals(this.unionDeliveryType, osellerProduct.unionDeliveryType) && Objects.equals(this.statusName, osellerProduct.statusName) && Objects.equals(this.sellerProductName, osellerProduct.sellerProductName) && Objects.equals(this.sellerProductId, osellerProduct.sellerProductId) && Objects.equals(this.saleStartedAt, osellerProduct.saleStartedAt) && Objects.equals(this.saleEndedAt, osellerProduct.saleEndedAt) && Objects.equals(this.returnZipCode, osellerProduct.returnZipCode) && Objects.equals(this.returnChargeVendor, osellerProduct.returnChargeVendor) && Objects.equals(this.returnChargeName, osellerProduct.returnChargeName) && Objects.equals(this.returnCharge, osellerProduct.returnCharge) && Objects.equals(this.returnCenterCode, osellerProduct.returnCenterCode) && Objects.equals(this.returnAddressDetail, osellerProduct.returnAddressDetail) && Objects.equals(this.returnAddress, osellerProduct.returnAddress) && Objects.equals(this.requiredDocuments, osellerProduct.requiredDocuments) && Objects.equals(this.requested, osellerProduct.requested) && Objects.equals(this.productId, osellerProduct.productId) && Objects.equals(this.outboundShippingPlaceCode, osellerProduct.outboundShippingPlaceCode) && Objects.equals(this.mdName, osellerProduct.mdName) && Objects.equals(this.mdId, osellerProduct.mdId) && Objects.equals(this.manufacture, osellerProduct.manufacture) && Objects.equals(this.extraInfoMessage, osellerProduct.extraInfoMessage) && Objects.equals(this.items, osellerProduct.items) && Objects.equals(this.freeShipOverAmount, osellerProduct.freeShipOverAmount) && Objects.equals(this.exchangeType, osellerProduct.exchangeType) && Objects.equals(this.displayCategoryCode, osellerProduct.displayCategoryCode) && Objects.equals(this.deliverySurcharge, osellerProduct.deliverySurcharge) && Objects.equals(this.deliveryMethod, osellerProduct.deliveryMethod) && Objects.equals(this.deliveryCompanyCode, osellerProduct.deliveryCompanyCode) && Objects.equals(this.deliveryChargeType, osellerProduct.deliveryChargeType) && Objects.equals(this.deliveryChargeOnReturn, osellerProduct.deliveryChargeOnReturn) && Objects.equals(this.deliveryCharge, osellerProduct.deliveryCharge) && Objects.equals(this.contributorType, osellerProduct.contributorType) && Objects.equals(this.companyContactNumber, osellerProduct.companyContactNumber) && Objects.equals(this.categoryId, osellerProduct.categoryId) && Objects.equals(this.bundlePackingDelivery, osellerProduct.bundlePackingDelivery) && Objects.equals(this.brand, osellerProduct.brand) && Objects.equals(this.afterServiceInformation, osellerProduct.afterServiceInformation) && Objects.equals(this.afterServiceContactNumber, osellerProduct.afterServiceContactNumber) && Objects.equals(this.remoteAreaDeliverable, osellerProduct.remoteAreaDeliverable);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.vendorUserId, this.vendorId, this.displayProductName, this.generalProductName, this.productGroup, this.unionDeliveryType, this.statusName, this.sellerProductName, this.sellerProductId, this.saleStartedAt, this.saleEndedAt, this.returnZipCode, this.returnChargeVendor, this.returnChargeName, this.returnCharge, this.returnCenterCode, this.returnAddressDetail, this.returnAddress, this.requiredDocuments, this.requested, this.productId, this.outboundShippingPlaceCode, this.mdName, this.mdId, this.manufacture, this.extraInfoMessage, this.items, this.freeShipOverAmount, this.exchangeType, this.displayCategoryCode, this.deliverySurcharge, this.deliveryMethod, this.deliveryCompanyCode, this.deliveryChargeType, this.deliveryChargeOnReturn, this.deliveryCharge, this.contributorType, this.companyContactNumber, this.categoryId, this.bundlePackingDelivery, this.brand, this.afterServiceInformation, this.afterServiceContactNumber, this.remoteAreaDeliverable});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OSellerProduct {\n");
        sb.append("    vendorUserId: ").append(this.toIndentedString(this.vendorUserId)).append("\n");
        sb.append("    vendorId: ").append(this.toIndentedString(this.vendorId)).append("\n");
        sb.append("    displayProductName: ").append(this.toIndentedString(this.displayProductName)).append("\n");
        sb.append("    generalProductName: ").append(this.toIndentedString(this.generalProductName)).append("\n");
        sb.append("    productGroup: ").append(this.toIndentedString(this.productGroup)).append("\n");
        sb.append("    unionDeliveryType: ").append(this.toIndentedString(this.unionDeliveryType)).append("\n");
        sb.append("    statusName: ").append(this.toIndentedString(this.statusName)).append("\n");
        sb.append("    sellerProductName: ").append(this.toIndentedString(this.sellerProductName)).append("\n");
        sb.append("    sellerProductId: ").append(this.toIndentedString(this.sellerProductId)).append("\n");
        sb.append("    saleStartedAt: ").append(this.toIndentedString(this.saleStartedAt)).append("\n");
        sb.append("    saleEndedAt: ").append(this.toIndentedString(this.saleEndedAt)).append("\n");
        sb.append("    returnZipCode: ").append(this.toIndentedString(this.returnZipCode)).append("\n");
        sb.append("    returnChargeVendor: ").append(this.toIndentedString(this.returnChargeVendor)).append("\n");
        sb.append("    returnChargeName: ").append(this.toIndentedString(this.returnChargeName)).append("\n");
        sb.append("    returnCharge: ").append(this.toIndentedString(this.returnCharge)).append("\n");
        sb.append("    returnCenterCode: ").append(this.toIndentedString(this.returnCenterCode)).append("\n");
        sb.append("    returnAddressDetail: ").append(this.toIndentedString(this.returnAddressDetail)).append("\n");
        sb.append("    returnAddress: ").append(this.toIndentedString(this.returnAddress)).append("\n");
        sb.append("    requiredDocuments: ").append(this.toIndentedString(this.requiredDocuments)).append("\n");
        sb.append("    requested: ").append(this.toIndentedString(this.requested)).append("\n");
        sb.append("    productId: ").append(this.toIndentedString(this.productId)).append("\n");
        sb.append("    outboundShippingPlaceCode: ").append(this.toIndentedString(this.outboundShippingPlaceCode)).append("\n");
        sb.append("    mdName: ").append(this.toIndentedString(this.mdName)).append("\n");
        sb.append("    mdId: ").append(this.toIndentedString(this.mdId)).append("\n");
        sb.append("    manufacture: ").append(this.toIndentedString(this.manufacture)).append("\n");
        sb.append("    extraInfoMessage: ").append(this.toIndentedString(this.extraInfoMessage)).append("\n");
        sb.append("    items: ").append(this.toIndentedString(this.items)).append("\n");
        sb.append("    freeShipOverAmount: ").append(this.toIndentedString(this.freeShipOverAmount)).append("\n");
        sb.append("    exchangeType: ").append(this.toIndentedString(this.exchangeType)).append("\n");
        sb.append("    displayCategoryCode: ").append(this.toIndentedString(this.displayCategoryCode)).append("\n");
        sb.append("    deliverySurcharge: ").append(this.toIndentedString(this.deliverySurcharge)).append("\n");
        sb.append("    deliveryMethod: ").append(this.toIndentedString(this.deliveryMethod)).append("\n");
        sb.append("    deliveryCompanyCode: ").append(this.toIndentedString(this.deliveryCompanyCode)).append("\n");
        sb.append("    deliveryChargeType: ").append(this.toIndentedString(this.deliveryChargeType)).append("\n");
        sb.append("    deliveryChargeOnReturn: ").append(this.toIndentedString(this.deliveryChargeOnReturn)).append("\n");
        sb.append("    deliveryCharge: ").append(this.toIndentedString(this.deliveryCharge)).append("\n");
        sb.append("    contributorType: ").append(this.toIndentedString(this.contributorType)).append("\n");
        sb.append("    companyContactNumber: ").append(this.toIndentedString(this.companyContactNumber)).append("\n");
        sb.append("    categoryId: ").append(this.toIndentedString(this.categoryId)).append("\n");
        sb.append("    bundlePackingDelivery: ").append(this.toIndentedString(this.bundlePackingDelivery)).append("\n");
        sb.append("    brand: ").append(this.toIndentedString(this.brand)).append("\n");
        sb.append("    afterServiceInformation: ").append(this.toIndentedString(this.afterServiceInformation)).append("\n");
        sb.append("    afterServiceContactNumber: ").append(this.toIndentedString(this.afterServiceContactNumber)).append("\n");
        sb.append("    remoteAreaDeliverable: ").append(this.toIndentedString(this.remoteAreaDeliverable)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static enum RemoteAreaDeliverableEnum {
        @SerializedName("Y")
        Y("Y"),
        @SerializedName("N")
        N("N");

        private String value;

        private RemoteAreaDeliverableEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static enum DeliveryChargeTypeEnum {
        @SerializedName("FREE")
        FREE("FREE"),
        @SerializedName("NOT_FREE")
        NOT_FREE("NOT_FREE"),
        @SerializedName("CHARGE_RECEIVED")
        CHARGE_RECEIVED("CHARGE_RECEIVED"),
        @SerializedName("CONDITIONAL_FREE")
        CONDITIONAL_FREE("CONDITIONAL_FREE"),
        @SerializedName("FREE_DELIVERY_OVER_9800")
        FREE_DELIVERY_OVER_9800("FREE_DELIVERY_OVER_9800");

        private String value;

        private DeliveryChargeTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static enum DeliveryMethodEnum {
        @SerializedName("SEQUENCIAL")
        SEQUENCIAL("SEQUENCIAL"),
        @SerializedName("VENDOR_DIRECT")
        VENDOR_DIRECT("VENDOR_DIRECT"),
        @SerializedName("MAKE_ORDER")
        MAKE_ORDER("MAKE_ORDER"),
        @SerializedName("INSTRUCTURE")
        INSTRUCTURE("INSTRUCTURE"),
        @SerializedName("AGENT_BUY")
        AGENT_BUY("AGENT_BUY"),
        @SerializedName("COLD_FRESH")
        COLD_FRESH("COLD_FRESH"),
        @SerializedName("MAKE_ORDER_DIRECT")
        MAKE_ORDER_DIRECT("MAKE_ORDER_DIRECT");

        private String value;

        private DeliveryMethodEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static enum ReturnChargeVendorEnum {
        @SerializedName("Y")
        Y("Y"),
        @SerializedName("N")
        N("N");

        private String value;

        private ReturnChargeVendorEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static enum UnionDeliveryTypeEnum {
        @SerializedName("UNION_DELIVERY")
        UNION_DELIVERY("UNION_DELIVERY"),
        @SerializedName("NOT_UNION_DELIVERY")
        NOT_UNION_DELIVERY("NOT_UNION_DELIVERY");

        private String value;

        private UnionDeliveryTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
