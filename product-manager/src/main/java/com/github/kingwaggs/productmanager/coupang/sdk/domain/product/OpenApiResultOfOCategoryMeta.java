package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OpenApiResultOfOCategoryMeta {
    @SerializedName("message")
    private String message = null;
    @SerializedName("data")
    private OCategoryMeta data = null;
    @SerializedName("code")
    private String code = null;

    public OpenApiResultOfOCategoryMeta() {
    }

    public OpenApiResultOfOCategoryMeta message(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OpenApiResultOfOCategoryMeta data(OCategoryMeta data) {
        this.data = data;
        return this;
    }

    public OCategoryMeta getData() {
        return this.data;
    }

    public void setData(OCategoryMeta data) {
        this.data = data;
    }

    public OpenApiResultOfOCategoryMeta code(String code) {
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
            OpenApiResultOfOCategoryMeta openApiResultOfOCategoryMeta = (OpenApiResultOfOCategoryMeta)o;
            return Objects.equals(this.message, openApiResultOfOCategoryMeta.message) && Objects.equals(this.data, openApiResultOfOCategoryMeta.data) && Objects.equals(this.code, openApiResultOfOCategoryMeta.code);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.message, this.data, this.code});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OpenApiResultOfOCategoryMeta {\n");
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
