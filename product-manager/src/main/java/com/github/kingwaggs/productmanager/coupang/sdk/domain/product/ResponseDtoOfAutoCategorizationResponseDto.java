package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ResponseDtoOfAutoCategorizationResponseDto {
    @SerializedName("code")
    private Integer code = null;
    @SerializedName("data")
    private AutoCategorizationResponseDto data = null;
    @SerializedName("message")
    private String message = null;

    public ResponseDtoOfAutoCategorizationResponseDto() {
    }

    public ResponseDtoOfAutoCategorizationResponseDto code(Integer code) {
        this.code = code;
        return this;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ResponseDtoOfAutoCategorizationResponseDto data(AutoCategorizationResponseDto data) {
        this.data = data;
        return this;
    }

    public AutoCategorizationResponseDto getData() {
        return this.data;
    }

    public void setData(AutoCategorizationResponseDto data) {
        this.data = data;
    }

    public ResponseDtoOfAutoCategorizationResponseDto message(String message) {
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
            ResponseDtoOfAutoCategorizationResponseDto responseDtoOfAutoCategorizationResponseDto = (ResponseDtoOfAutoCategorizationResponseDto)o;
            return Objects.equals(this.code, responseDtoOfAutoCategorizationResponseDto.code) && Objects.equals(this.data, responseDtoOfAutoCategorizationResponseDto.data) && Objects.equals(this.message, responseDtoOfAutoCategorizationResponseDto.message);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.code, this.data, this.message});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResponseDtoOfAutoCategorizationResponseDto {\n");
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
