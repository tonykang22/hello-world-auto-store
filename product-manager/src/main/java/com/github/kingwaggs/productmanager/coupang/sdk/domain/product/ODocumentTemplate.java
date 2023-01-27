package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ODocumentTemplate {
    @SerializedName("templateName")
    private String templateName = null;
    @SerializedName("required")
    private String required = null;

    public ODocumentTemplate() {
    }

    public ODocumentTemplate templateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public ODocumentTemplate required(String required) {
        this.required = required;
        return this;
    }

    public String getRequired() {
        return this.required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ODocumentTemplate odocumentTemplate = (ODocumentTemplate)o;
            return Objects.equals(this.templateName, odocumentTemplate.templateName) && Objects.equals(this.required, odocumentTemplate.required);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.templateName, this.required});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ODocumentTemplate {\n");
        sb.append("    templateName: ").append(this.toIndentedString(this.templateName)).append("\n");
        sb.append("    required: ").append(this.toIndentedString(this.required)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
