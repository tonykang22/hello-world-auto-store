package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OutboundInquiryReturn {
    @SerializedName("createDate")
    private String createDate = null;
    @SerializedName("outboundShippingPlaceCode")
    private Long outboundShippingPlaceCode = null;
    @SerializedName("placeAddresses")
    private List<PlaceAddressDto> placeAddresses = new ArrayList();
    @SerializedName("remoteInfos")
    private List<RemoteInfoDto> remoteInfos = new ArrayList();
    @SerializedName("shippingPlaceName")
    private String shippingPlaceName = null;
    @SerializedName("usable")
    private Boolean usable = null;

    public OutboundInquiryReturn() {
    }

    public OutboundInquiryReturn createDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public OutboundInquiryReturn outboundShippingPlaceCode(Long outboundShippingPlaceCode) {
        this.outboundShippingPlaceCode = outboundShippingPlaceCode;
        return this;
    }

    public Long getOutboundShippingPlaceCode() {
        return this.outboundShippingPlaceCode;
    }

    public void setOutboundShippingPlaceCode(Long outboundShippingPlaceCode) {
        this.outboundShippingPlaceCode = outboundShippingPlaceCode;
    }

    public OutboundInquiryReturn placeAddresses(List<PlaceAddressDto> placeAddresses) {
        this.placeAddresses = placeAddresses;
        return this;
    }

    public OutboundInquiryReturn addPlaceAddressesItem(PlaceAddressDto placeAddressesItem) {
        this.placeAddresses.add(placeAddressesItem);
        return this;
    }

    public List<PlaceAddressDto> getPlaceAddresses() {
        return this.placeAddresses;
    }

    public void setPlaceAddresses(List<PlaceAddressDto> placeAddresses) {
        this.placeAddresses = placeAddresses;
    }

    public OutboundInquiryReturn remoteInfos(List<RemoteInfoDto> remoteInfos) {
        this.remoteInfos = remoteInfos;
        return this;
    }

    public OutboundInquiryReturn addRemoteInfosItem(RemoteInfoDto remoteInfosItem) {
        this.remoteInfos.add(remoteInfosItem);
        return this;
    }

    public List<RemoteInfoDto> getRemoteInfos() {
        return this.remoteInfos;
    }

    public void setRemoteInfos(List<RemoteInfoDto> remoteInfos) {
        this.remoteInfos = remoteInfos;
    }

    public OutboundInquiryReturn shippingPlaceName(String shippingPlaceName) {
        this.shippingPlaceName = shippingPlaceName;
        return this;
    }

    public String getShippingPlaceName() {
        return this.shippingPlaceName;
    }

    public void setShippingPlaceName(String shippingPlaceName) {
        this.shippingPlaceName = shippingPlaceName;
    }

    public OutboundInquiryReturn usable(Boolean usable) {
        this.usable = usable;
        return this;
    }

    public Boolean getUsable() {
        return this.usable;
    }

    public void setUsable(Boolean usable) {
        this.usable = usable;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OutboundInquiryReturn outboundInquiryReturn = (OutboundInquiryReturn)o;
            return Objects.equals(this.createDate, outboundInquiryReturn.createDate) && Objects.equals(this.outboundShippingPlaceCode, outboundInquiryReturn.outboundShippingPlaceCode) && Objects.equals(this.placeAddresses, outboundInquiryReturn.placeAddresses) && Objects.equals(this.remoteInfos, outboundInquiryReturn.remoteInfos) && Objects.equals(this.shippingPlaceName, outboundInquiryReturn.shippingPlaceName) && Objects.equals(this.usable, outboundInquiryReturn.usable);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.createDate, this.outboundShippingPlaceCode, this.placeAddresses, this.remoteInfos, this.shippingPlaceName, this.usable});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OutboundInquiryReturn {\n");
        sb.append("    createDate: ").append(this.toIndentedString(this.createDate)).append("\n");
        sb.append("    outboundShippingPlaceCode: ").append(this.toIndentedString(this.outboundShippingPlaceCode)).append("\n");
        sb.append("    placeAddresses: ").append(this.toIndentedString(this.placeAddresses)).append("\n");
        sb.append("    remoteInfos: ").append(this.toIndentedString(this.remoteInfos)).append("\n");
        sb.append("    shippingPlaceName: ").append(this.toIndentedString(this.shippingPlaceName)).append("\n");
        sb.append("    usable: ").append(this.toIndentedString(this.usable)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
