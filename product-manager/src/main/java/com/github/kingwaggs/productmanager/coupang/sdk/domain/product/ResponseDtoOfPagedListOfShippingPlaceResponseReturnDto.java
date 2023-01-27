package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto {
    @SerializedName("code")
    private Integer code = null;
    @SerializedName("data")
    private PagedListOfShippingPlaceResponseReturnDto data = null;
    @SerializedName("message")
    private String message = null;

    public ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto() {
    }

    public ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto code(Integer code) {
        this.code = code;
        return this;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto data(PagedListOfShippingPlaceResponseReturnDto data) {
        this.data = data;
        return this;
    }

    public PagedListOfShippingPlaceResponseReturnDto getData() {
        return this.data;
    }

    public void setData(PagedListOfShippingPlaceResponseReturnDto data) {
        this.data = data;
    }

    public ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto message(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto responseDtoOfPagedListOfShippingPlaceResponseReturnDto = (ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto)o;
            return Objects.equals(this.code, responseDtoOfPagedListOfShippingPlaceResponseReturnDto.code) && Objects.equals(this.data, responseDtoOfPagedListOfShippingPlaceResponseReturnDto.data) && Objects.equals(this.message, responseDtoOfPagedListOfShippingPlaceResponseReturnDto.message);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.code, this.data, this.message});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto {\n");
        sb.append("    code: ").append(this.toIndentedString(this.code)).append("\n");
        sb.append("    data: ").append(this.toIndentedString(this.data)).append("\n");
        sb.append("    message: ").append(this.toIndentedString(this.message)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
