package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShippingPlaceResponseReturnDto {
    @SerializedName("consumerCashFee05kg")
    private Integer consumerCashFee05kg = null;
    @SerializedName("consumerCashFee10kg")
    private Integer consumerCashFee10kg = null;
    @SerializedName("consumerCashFee20kg")
    private Integer consumerCashFee20kg = null;
    @SerializedName("createdAt")
    private String createdAt = null;
    @SerializedName("deliverCode")
    private String deliverCode = null;
    @SerializedName("deliverName")
    private String deliverName = null;
    @SerializedName("errorMessage")
    private String errorMessage = null;
    @SerializedName("goodsflowStatus")
    private String goodsflowStatus = null;
    @SerializedName("placeAddresses")
    private List<PlaceAddressDto> placeAddresses = new ArrayList();
    @SerializedName("returnCenterCode")
    private String returnCenterCode = null;
    @SerializedName("returnFee05kg")
    private Integer returnFee05kg = null;
    @SerializedName("returnFee10kg")
    private Integer returnFee10kg = null;
    @SerializedName("returnFee20kg")
    private Integer returnFee20kg = null;
    @SerializedName("shippingPlaceName")
    private String shippingPlaceName = null;
    @SerializedName("usable")
    private Boolean usable = null;
    @SerializedName("vendorCashFee05kg")
    private Integer vendorCashFee05kg = null;
    @SerializedName("vendorCashFee10kg")
    private Integer vendorCashFee10kg = null;
    @SerializedName("vendorCashFee20kg")
    private Integer vendorCashFee20kg = null;
    @SerializedName("vendorCreditFee05kg")
    private Integer vendorCreditFee05kg = null;
    @SerializedName("vendorCreditFee10kg")
    private Integer vendorCreditFee10kg = null;
    @SerializedName("vendorCreditFee20kg")
    private Integer vendorCreditFee20kg = null;
    @SerializedName("vendorId")
    private String vendorId = null;

    public ShippingPlaceResponseReturnDto() {
    }

    public ShippingPlaceResponseReturnDto consumerCashFee05kg(Integer consumerCashFee05kg) {
        this.consumerCashFee05kg = consumerCashFee05kg;
        return this;
    }

    public Integer getConsumerCashFee05kg() {
        return this.consumerCashFee05kg;
    }

    public void setConsumerCashFee05kg(Integer consumerCashFee05kg) {
        this.consumerCashFee05kg = consumerCashFee05kg;
    }

    public ShippingPlaceResponseReturnDto consumerCashFee10kg(Integer consumerCashFee10kg) {
        this.consumerCashFee10kg = consumerCashFee10kg;
        return this;
    }

    public Integer getConsumerCashFee10kg() {
        return this.consumerCashFee10kg;
    }

    public void setConsumerCashFee10kg(Integer consumerCashFee10kg) {
        this.consumerCashFee10kg = consumerCashFee10kg;
    }

    public ShippingPlaceResponseReturnDto consumerCashFee20kg(Integer consumerCashFee20kg) {
        this.consumerCashFee20kg = consumerCashFee20kg;
        return this;
    }

    public Integer getConsumerCashFee20kg() {
        return this.consumerCashFee20kg;
    }

    public void setConsumerCashFee20kg(Integer consumerCashFee20kg) {
        this.consumerCashFee20kg = consumerCashFee20kg;
    }

    public ShippingPlaceResponseReturnDto createdAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ShippingPlaceResponseReturnDto deliverCode(String deliverCode) {
        this.deliverCode = deliverCode;
        return this;
    }

    public String getDeliverCode() {
        return this.deliverCode;
    }

    public void setDeliverCode(String deliverCode) {
        this.deliverCode = deliverCode;
    }

    public ShippingPlaceResponseReturnDto deliverName(String deliverName) {
        this.deliverName = deliverName;
        return this;
    }

    public String getDeliverName() {
        return this.deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
    }

    public ShippingPlaceResponseReturnDto errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ShippingPlaceResponseReturnDto goodsflowStatus(String goodsflowStatus) {
        this.goodsflowStatus = goodsflowStatus;
        return this;
    }

    public String getGoodsflowStatus() {
        return this.goodsflowStatus;
    }

    public void setGoodsflowStatus(String goodsflowStatus) {
        this.goodsflowStatus = goodsflowStatus;
    }

    public ShippingPlaceResponseReturnDto placeAddresses(List<PlaceAddressDto> placeAddresses) {
        this.placeAddresses = placeAddresses;
        return this;
    }

    public ShippingPlaceResponseReturnDto addPlaceAddressesItem(PlaceAddressDto placeAddressesItem) {
        this.placeAddresses.add(placeAddressesItem);
        return this;
    }

    public List<PlaceAddressDto> getPlaceAddresses() {
        return this.placeAddresses;
    }

    public void setPlaceAddresses(List<PlaceAddressDto> placeAddresses) {
        this.placeAddresses = placeAddresses;
    }

    public ShippingPlaceResponseReturnDto returnCenterCode(String returnCenterCode) {
        this.returnCenterCode = returnCenterCode;
        return this;
    }

    public String getReturnCenterCode() {
        return this.returnCenterCode;
    }

    public void setReturnCenterCode(String returnCenterCode) {
        this.returnCenterCode = returnCenterCode;
    }

    public ShippingPlaceResponseReturnDto returnFee05kg(Integer returnFee05kg) {
        this.returnFee05kg = returnFee05kg;
        return this;
    }

    public Integer getReturnFee05kg() {
        return this.returnFee05kg;
    }

    public void setReturnFee05kg(Integer returnFee05kg) {
        this.returnFee05kg = returnFee05kg;
    }

    public ShippingPlaceResponseReturnDto returnFee10kg(Integer returnFee10kg) {
        this.returnFee10kg = returnFee10kg;
        return this;
    }

    public Integer getReturnFee10kg() {
        return this.returnFee10kg;
    }

    public void setReturnFee10kg(Integer returnFee10kg) {
        this.returnFee10kg = returnFee10kg;
    }

    public ShippingPlaceResponseReturnDto returnFee20kg(Integer returnFee20kg) {
        this.returnFee20kg = returnFee20kg;
        return this;
    }

    public Integer getReturnFee20kg() {
        return this.returnFee20kg;
    }

    public void setReturnFee20kg(Integer returnFee20kg) {
        this.returnFee20kg = returnFee20kg;
    }

    public ShippingPlaceResponseReturnDto shippingPlaceName(String shippingPlaceName) {
        this.shippingPlaceName = shippingPlaceName;
        return this;
    }

    public String getShippingPlaceName() {
        return this.shippingPlaceName;
    }

    public void setShippingPlaceName(String shippingPlaceName) {
        this.shippingPlaceName = shippingPlaceName;
    }

    public ShippingPlaceResponseReturnDto usable(Boolean usable) {
        this.usable = usable;
        return this;
    }

    public Boolean getUsable() {
        return this.usable;
    }

    public void setUsable(Boolean usable) {
        this.usable = usable;
    }

    public ShippingPlaceResponseReturnDto vendorCashFee05kg(Integer vendorCashFee05kg) {
        this.vendorCashFee05kg = vendorCashFee05kg;
        return this;
    }

    public Integer getVendorCashFee05kg() {
        return this.vendorCashFee05kg;
    }

    public void setVendorCashFee05kg(Integer vendorCashFee05kg) {
        this.vendorCashFee05kg = vendorCashFee05kg;
    }

    public ShippingPlaceResponseReturnDto vendorCashFee10kg(Integer vendorCashFee10kg) {
        this.vendorCashFee10kg = vendorCashFee10kg;
        return this;
    }

    public Integer getVendorCashFee10kg() {
        return this.vendorCashFee10kg;
    }

    public void setVendorCashFee10kg(Integer vendorCashFee10kg) {
        this.vendorCashFee10kg = vendorCashFee10kg;
    }

    public ShippingPlaceResponseReturnDto vendorCashFee20kg(Integer vendorCashFee20kg) {
        this.vendorCashFee20kg = vendorCashFee20kg;
        return this;
    }

    public Integer getVendorCashFee20kg() {
        return this.vendorCashFee20kg;
    }

    public void setVendorCashFee20kg(Integer vendorCashFee20kg) {
        this.vendorCashFee20kg = vendorCashFee20kg;
    }

    public ShippingPlaceResponseReturnDto vendorCreditFee05kg(Integer vendorCreditFee05kg) {
        this.vendorCreditFee05kg = vendorCreditFee05kg;
        return this;
    }

    public Integer getVendorCreditFee05kg() {
        return this.vendorCreditFee05kg;
    }

    public void setVendorCreditFee05kg(Integer vendorCreditFee05kg) {
        this.vendorCreditFee05kg = vendorCreditFee05kg;
    }

    public ShippingPlaceResponseReturnDto vendorCreditFee10kg(Integer vendorCreditFee10kg) {
        this.vendorCreditFee10kg = vendorCreditFee10kg;
        return this;
    }

    public Integer getVendorCreditFee10kg() {
        return this.vendorCreditFee10kg;
    }

    public void setVendorCreditFee10kg(Integer vendorCreditFee10kg) {
        this.vendorCreditFee10kg = vendorCreditFee10kg;
    }

    public ShippingPlaceResponseReturnDto vendorCreditFee20kg(Integer vendorCreditFee20kg) {
        this.vendorCreditFee20kg = vendorCreditFee20kg;
        return this;
    }

    public Integer getVendorCreditFee20kg() {
        return this.vendorCreditFee20kg;
    }

    public void setVendorCreditFee20kg(Integer vendorCreditFee20kg) {
        this.vendorCreditFee20kg = vendorCreditFee20kg;
    }

    public ShippingPlaceResponseReturnDto vendorId(String vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ShippingPlaceResponseReturnDto shippingPlaceResponseReturnDto = (ShippingPlaceResponseReturnDto)o;
            return Objects.equals(this.consumerCashFee05kg, shippingPlaceResponseReturnDto.consumerCashFee05kg) && Objects.equals(this.consumerCashFee10kg, shippingPlaceResponseReturnDto.consumerCashFee10kg) && Objects.equals(this.consumerCashFee20kg, shippingPlaceResponseReturnDto.consumerCashFee20kg) && Objects.equals(this.createdAt, shippingPlaceResponseReturnDto.createdAt) && Objects.equals(this.deliverCode, shippingPlaceResponseReturnDto.deliverCode) && Objects.equals(this.deliverName, shippingPlaceResponseReturnDto.deliverName) && Objects.equals(this.errorMessage, shippingPlaceResponseReturnDto.errorMessage) && Objects.equals(this.goodsflowStatus, shippingPlaceResponseReturnDto.goodsflowStatus) && Objects.equals(this.placeAddresses, shippingPlaceResponseReturnDto.placeAddresses) && Objects.equals(this.returnCenterCode, shippingPlaceResponseReturnDto.returnCenterCode) && Objects.equals(this.returnFee05kg, shippingPlaceResponseReturnDto.returnFee05kg) && Objects.equals(this.returnFee10kg, shippingPlaceResponseReturnDto.returnFee10kg) && Objects.equals(this.returnFee20kg, shippingPlaceResponseReturnDto.returnFee20kg) && Objects.equals(this.shippingPlaceName, shippingPlaceResponseReturnDto.shippingPlaceName) && Objects.equals(this.usable, shippingPlaceResponseReturnDto.usable) && Objects.equals(this.vendorCashFee05kg, shippingPlaceResponseReturnDto.vendorCashFee05kg) && Objects.equals(this.vendorCashFee10kg, shippingPlaceResponseReturnDto.vendorCashFee10kg) && Objects.equals(this.vendorCashFee20kg, shippingPlaceResponseReturnDto.vendorCashFee20kg) && Objects.equals(this.vendorCreditFee05kg, shippingPlaceResponseReturnDto.vendorCreditFee05kg) && Objects.equals(this.vendorCreditFee10kg, shippingPlaceResponseReturnDto.vendorCreditFee10kg) && Objects.equals(this.vendorCreditFee20kg, shippingPlaceResponseReturnDto.vendorCreditFee20kg) && Objects.equals(this.vendorId, shippingPlaceResponseReturnDto.vendorId);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.consumerCashFee05kg, this.consumerCashFee10kg, this.consumerCashFee20kg, this.createdAt, this.deliverCode, this.deliverName, this.errorMessage, this.goodsflowStatus, this.placeAddresses, this.returnCenterCode, this.returnFee05kg, this.returnFee10kg, this.returnFee20kg, this.shippingPlaceName, this.usable, this.vendorCashFee05kg, this.vendorCashFee10kg, this.vendorCashFee20kg, this.vendorCreditFee05kg, this.vendorCreditFee10kg, this.vendorCreditFee20kg, this.vendorId});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShippingPlaceResponseReturnDto {\n");
        sb.append("    consumerCashFee05kg: ").append(this.toIndentedString(this.consumerCashFee05kg)).append("\n");
        sb.append("    consumerCashFee10kg: ").append(this.toIndentedString(this.consumerCashFee10kg)).append("\n");
        sb.append("    consumerCashFee20kg: ").append(this.toIndentedString(this.consumerCashFee20kg)).append("\n");
        sb.append("    createdAt: ").append(this.toIndentedString(this.createdAt)).append("\n");
        sb.append("    deliverCode: ").append(this.toIndentedString(this.deliverCode)).append("\n");
        sb.append("    deliverName: ").append(this.toIndentedString(this.deliverName)).append("\n");
        sb.append("    errorMessage: ").append(this.toIndentedString(this.errorMessage)).append("\n");
        sb.append("    goodsflowStatus: ").append(this.toIndentedString(this.goodsflowStatus)).append("\n");
        sb.append("    placeAddresses: ").append(this.toIndentedString(this.placeAddresses)).append("\n");
        sb.append("    returnCenterCode: ").append(this.toIndentedString(this.returnCenterCode)).append("\n");
        sb.append("    returnFee05kg: ").append(this.toIndentedString(this.returnFee05kg)).append("\n");
        sb.append("    returnFee10kg: ").append(this.toIndentedString(this.returnFee10kg)).append("\n");
        sb.append("    returnFee20kg: ").append(this.toIndentedString(this.returnFee20kg)).append("\n");
        sb.append("    shippingPlaceName: ").append(this.toIndentedString(this.shippingPlaceName)).append("\n");
        sb.append("    usable: ").append(this.toIndentedString(this.usable)).append("\n");
        sb.append("    vendorCashFee05kg: ").append(this.toIndentedString(this.vendorCashFee05kg)).append("\n");
        sb.append("    vendorCashFee10kg: ").append(this.toIndentedString(this.vendorCashFee10kg)).append("\n");
        sb.append("    vendorCashFee20kg: ").append(this.toIndentedString(this.vendorCashFee20kg)).append("\n");
        sb.append("    vendorCreditFee05kg: ").append(this.toIndentedString(this.vendorCreditFee05kg)).append("\n");
        sb.append("    vendorCreditFee10kg: ").append(this.toIndentedString(this.vendorCreditFee10kg)).append("\n");
        sb.append("    vendorCreditFee20kg: ").append(this.toIndentedString(this.vendorCreditFee20kg)).append("\n");
        sb.append("    vendorId: ").append(this.toIndentedString(this.vendorId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
