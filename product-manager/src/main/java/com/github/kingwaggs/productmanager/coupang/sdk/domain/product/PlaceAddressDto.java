package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class PlaceAddressDto {
    @SerializedName("addressType")
    private String addressType = null;
    @SerializedName("companyContactNumber")
    private String companyContactNumber = null;
    @SerializedName("countryCode")
    private String countryCode = null;
    @SerializedName("phoneNumber2")
    private String phoneNumber2 = null;
    @SerializedName("returnAddress")
    private String returnAddress = null;
    @SerializedName("returnAddressDetail")
    private String returnAddressDetail = null;
    @SerializedName("returnZipCode")
    private String returnZipCode = null;

    public PlaceAddressDto() {
    }

    public PlaceAddressDto addressType(String addressType) {
        this.addressType = addressType;
        return this;
    }

    public String getAddressType() {
        return this.addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public PlaceAddressDto companyContactNumber(String companyContactNumber) {
        this.companyContactNumber = companyContactNumber;
        return this;
    }

    public String getCompanyContactNumber() {
        return this.companyContactNumber;
    }

    public void setCompanyContactNumber(String companyContactNumber) {
        this.companyContactNumber = companyContactNumber;
    }

    public PlaceAddressDto countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public PlaceAddressDto phoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
        return this;
    }

    public String getPhoneNumber2() {
        return this.phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public PlaceAddressDto returnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
        return this;
    }

    public String getReturnAddress() {
        return this.returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public PlaceAddressDto returnAddressDetail(String returnAddressDetail) {
        this.returnAddressDetail = returnAddressDetail;
        return this;
    }

    public String getReturnAddressDetail() {
        return this.returnAddressDetail;
    }

    public void setReturnAddressDetail(String returnAddressDetail) {
        this.returnAddressDetail = returnAddressDetail;
    }

    public PlaceAddressDto returnZipCode(String returnZipCode) {
        this.returnZipCode = returnZipCode;
        return this;
    }

    public String getReturnZipCode() {
        return this.returnZipCode;
    }

    public void setReturnZipCode(String returnZipCode) {
        this.returnZipCode = returnZipCode;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PlaceAddressDto placeAddressDto = (PlaceAddressDto)o;
            return Objects.equals(this.addressType, placeAddressDto.addressType) && Objects.equals(this.companyContactNumber, placeAddressDto.companyContactNumber) && Objects.equals(this.countryCode, placeAddressDto.countryCode) && Objects.equals(this.phoneNumber2, placeAddressDto.phoneNumber2) && Objects.equals(this.returnAddress, placeAddressDto.returnAddress) && Objects.equals(this.returnAddressDetail, placeAddressDto.returnAddressDetail) && Objects.equals(this.returnZipCode, placeAddressDto.returnZipCode);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.addressType, this.companyContactNumber, this.countryCode, this.phoneNumber2, this.returnAddress, this.returnAddressDetail, this.returnZipCode});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PlaceAddressDto {\n");
        sb.append("    addressType: ").append(this.toIndentedString(this.addressType)).append("\n");
        sb.append("    companyContactNumber: ").append(this.toIndentedString(this.companyContactNumber)).append("\n");
        sb.append("    countryCode: ").append(this.toIndentedString(this.countryCode)).append("\n");
        sb.append("    phoneNumber2: ").append(this.toIndentedString(this.phoneNumber2)).append("\n");
        sb.append("    returnAddress: ").append(this.toIndentedString(this.returnAddress)).append("\n");
        sb.append("    returnAddressDetail: ").append(this.toIndentedString(this.returnAddressDetail)).append("\n");
        sb.append("    returnZipCode: ").append(this.toIndentedString(this.returnZipCode)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
