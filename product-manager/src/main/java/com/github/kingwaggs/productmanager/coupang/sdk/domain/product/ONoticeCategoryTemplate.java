package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ONoticeCategoryTemplate {
    @SerializedName("required")
    private String required = null;
    @SerializedName("noticeCategoryDetailName")
    private String noticeCategoryDetailName = null;

    public ONoticeCategoryTemplate() {
    }

    public ONoticeCategoryTemplate required(String required) {
        this.required = required;
        return this;
    }

    public String getRequired() {
        return this.required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public ONoticeCategoryTemplate noticeCategoryDetailName(String noticeCategoryDetailName) {
        this.noticeCategoryDetailName = noticeCategoryDetailName;
        return this;
    }

    public String getNoticeCategoryDetailName() {
        return this.noticeCategoryDetailName;
    }

    public void setNoticeCategoryDetailName(String noticeCategoryDetailName) {
        this.noticeCategoryDetailName = noticeCategoryDetailName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ONoticeCategoryTemplate onoticeCategoryTemplate = (ONoticeCategoryTemplate)o;
            return Objects.equals(this.required, onoticeCategoryTemplate.required) && Objects.equals(this.noticeCategoryDetailName, onoticeCategoryTemplate.noticeCategoryDetailName);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.required, this.noticeCategoryDetailName});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ONoticeCategoryTemplate {\n");
        sb.append("    required: ").append(this.toIndentedString(this.required)).append("\n");
        sb.append("    noticeCategoryDetailName: ").append(this.toIndentedString(this.noticeCategoryDetailName)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
