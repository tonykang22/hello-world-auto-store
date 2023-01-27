package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OSellerProductItemAttribute {
    @SerializedName("attributeValueName")
    private String attributeValueName = null;
    @SerializedName("attributeTypeName")
    private String attributeTypeName = null;
    @SerializedName("exposed")
    private OSellerProductItemAttribute.ExposedEnum exposed = null;
    @SerializedName("editable")
    private String editable = null;

    public OSellerProductItemAttribute() {
    }

    public OSellerProductItemAttribute attributeValueName(String attributeValueName) {
        this.attributeValueName = attributeValueName;
        return this;
    }

    public String getAttributeValueName() {
        return this.attributeValueName;
    }

    public void setAttributeValueName(String attributeValueName) {
        this.attributeValueName = attributeValueName;
    }

    public OSellerProductItemAttribute attributeTypeName(String attributeTypeName) {
        this.attributeTypeName = attributeTypeName;
        return this;
    }

    public String getAttributeTypeName() {
        return this.attributeTypeName;
    }

    public void setAttributeTypeName(String attributeTypeName) {
        this.attributeTypeName = attributeTypeName;
    }

    public OSellerProductItemAttribute exposed(OSellerProductItemAttribute.ExposedEnum exposed) {
        this.exposed = exposed;
        return this;
    }

    public OSellerProductItemAttribute.ExposedEnum getExposed() {
        return this.exposed;
    }

    public void setExposed(OSellerProductItemAttribute.ExposedEnum exposed) {
        this.exposed = exposed;
    }

    public OSellerProductItemAttribute editable(String editable) {
        this.editable = editable;
        return this;
    }

    public String getEditable() {
        return this.editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OSellerProductItemAttribute osellerProductItemAttribute = (OSellerProductItemAttribute)o;
            return Objects.equals(this.attributeValueName, osellerProductItemAttribute.attributeValueName) && Objects.equals(this.attributeTypeName, osellerProductItemAttribute.attributeTypeName) && Objects.equals(this.exposed, osellerProductItemAttribute.exposed) && Objects.equals(this.editable, osellerProductItemAttribute.editable);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.attributeValueName, this.attributeTypeName, this.exposed, this.editable});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OSellerProductItemAttribute {\n");
        sb.append("    attributeValueName: ").append(this.toIndentedString(this.attributeValueName)).append("\n");
        sb.append("    attributeTypeName: ").append(this.toIndentedString(this.attributeTypeName)).append("\n");
        sb.append("    exposed: ").append(this.toIndentedString(this.exposed)).append("\n");
        sb.append("    editable: ").append(this.toIndentedString(this.editable)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static enum ExposedEnum {
        @SerializedName("EXPOSED")
        EXPOSED("EXPOSED"),
        @SerializedName("NONE")
        NONE("NONE");

        private String value;

        private ExposedEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
