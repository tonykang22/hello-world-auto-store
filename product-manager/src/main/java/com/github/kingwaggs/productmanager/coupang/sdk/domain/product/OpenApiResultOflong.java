package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OpenApiResultOflong {

    @SerializedName("message")
    private String message = null;
    @SerializedName("data")
    private Long data = null;
    @SerializedName("code")
    private String code = null;

    public OpenApiResultOflong() {
    }

    public OpenApiResultOflong message(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OpenApiResultOflong data(Long data) {
        this.data = data;
        return this;
    }

    public Long getData() {
        return this.data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    public OpenApiResultOflong code(String code) {
        this.code = code;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OpenApiResultOflong openApiResultOflong = (OpenApiResultOflong)o;
            return Objects.equals(this.message, openApiResultOflong.message) && Objects.equals(this.data, openApiResultOflong.data) && Objects.equals(this.code, openApiResultOflong.code);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.message, this.data, this.code});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OpenApiResultOflong {\n");
        sb.append("    message: ").append(this.toIndentedString(this.message)).append("\n");
        sb.append("    data: ").append(this.toIndentedString(this.data)).append("\n");
        sb.append("    code: ").append(this.toIndentedString(this.code)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
