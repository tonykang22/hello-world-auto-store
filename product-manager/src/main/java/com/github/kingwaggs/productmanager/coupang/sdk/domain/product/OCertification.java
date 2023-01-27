package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OCertification {
    @SerializedName("required")
    private String required = null;
    @SerializedName("name")
    private String name = null;
    @SerializedName("dataType")
    private String dataType = null;
    @SerializedName("certificationType")
    private String certificationType = null;

    public OCertification() {
    }

    public OCertification required(String required) {
        this.required = required;
        return this;
    }

    public String getRequired() {
        return this.required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public OCertification name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OCertification dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public OCertification certificationType(String certificationType) {
        this.certificationType = certificationType;
        return this;
    }

    public String getCertificationType() {
        return this.certificationType;
    }

    public void setCertificationType(String certificationType) {
        this.certificationType = certificationType;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OCertification ocertification = (OCertification)o;
            return Objects.equals(this.required, ocertification.required) && Objects.equals(this.name, ocertification.name) && Objects.equals(this.dataType, ocertification.dataType) && Objects.equals(this.certificationType, ocertification.certificationType);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.required, this.name, this.dataType, this.certificationType});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OCertification {\n");
        sb.append("    required: ").append(this.toIndentedString(this.required)).append("\n");
        sb.append("    name: ").append(this.toIndentedString(this.name)).append("\n");
        sb.append("    dataType: ").append(this.toIndentedString(this.dataType)).append("\n");
        sb.append("    certificationType: ").append(this.toIndentedString(this.certificationType)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
