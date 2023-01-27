package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OAttributeType {
    @SerializedName("usableUnits")
    private List<String> usableUnits = new ArrayList();
    @SerializedName("required")
    private String required = null;
    @SerializedName("dataType")
    private String dataType = null;
    @SerializedName("basicUnit")
    private String basicUnit = null;
    @SerializedName("attributeTypeName")
    private String attributeTypeName = null;
    @SerializedName("groupNumber")
    private String groupNumber = null;
    @SerializedName("exposed")
    private String exposed = null;

    public OAttributeType() {
    }

    public OAttributeType usableUnits(List<String> usableUnits) {
        this.usableUnits = usableUnits;
        return this;
    }

    public OAttributeType addUsableUnitsItem(String usableUnitsItem) {
        this.usableUnits.add(usableUnitsItem);
        return this;
    }

    public List<String> getUsableUnits() {
        return this.usableUnits;
    }

    public void setUsableUnits(List<String> usableUnits) {
        this.usableUnits = usableUnits;
    }

    public OAttributeType required(String required) {
        this.required = required;
        return this;
    }

    public String getRequired() {
        return this.required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public OAttributeType dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public OAttributeType basicUnit(String basicUnit) {
        this.basicUnit = basicUnit;
        return this;
    }

    public String getBasicUnit() {
        return this.basicUnit;
    }

    public void setBasicUnit(String basicUnit) {
        this.basicUnit = basicUnit;
    }

    public OAttributeType attributeTypeName(String attributeTypeName) {
        this.attributeTypeName = attributeTypeName;
        return this;
    }

    public String getAttributeTypeName() {
        return this.attributeTypeName;
    }

    public void setAttributeTypeName(String attributeTypeName) {
        this.attributeTypeName = attributeTypeName;
    }

    public OAttributeType groupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
        return this;
    }

    public String getGroupNumber() {
        return this.groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public OAttributeType exposed(String exposed) {
        this.exposed = exposed;
        return this;
    }

    public String getExposed() {
        return this.exposed;
    }

    public void setExposed(String exposed) {
        this.exposed = exposed;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OAttributeType oattributeType = (OAttributeType)o;
            return Objects.equals(this.usableUnits, oattributeType.usableUnits) && Objects.equals(this.required, oattributeType.required) && Objects.equals(this.dataType, oattributeType.dataType) && Objects.equals(this.basicUnit, oattributeType.basicUnit) && Objects.equals(this.attributeTypeName, oattributeType.attributeTypeName) && Objects.equals(this.groupNumber, oattributeType.groupNumber) && Objects.equals(this.exposed, oattributeType.exposed);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.usableUnits, this.required, this.dataType, this.basicUnit, this.attributeTypeName, this.groupNumber, this.exposed});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OAttributeType {\n");
        sb.append("    usableUnits: ").append(this.toIndentedString(this.usableUnits)).append("\n");
        sb.append("    required: ").append(this.toIndentedString(this.required)).append("\n");
        sb.append("    dataType: ").append(this.toIndentedString(this.dataType)).append("\n");
        sb.append("    basicUnit: ").append(this.toIndentedString(this.basicUnit)).append("\n");
        sb.append("    attributeTypeName: ").append(this.toIndentedString(this.attributeTypeName)).append("\n");
        sb.append("    groupNumber: ").append(this.toIndentedString(this.groupNumber)).append("\n");
        sb.append("    exposed: ").append(this.toIndentedString(this.exposed)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
