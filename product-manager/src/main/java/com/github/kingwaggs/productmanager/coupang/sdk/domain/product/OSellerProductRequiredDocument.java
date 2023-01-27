package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OSellerProductRequiredDocument {
    @SerializedName("vendorDocumentPath")
    private String vendorDocumentPath = null;
    @SerializedName("templateName")
    private String templateName = null;
    @SerializedName("documentPath")
    private String documentPath = null;

    public OSellerProductRequiredDocument() {
    }

    public OSellerProductRequiredDocument vendorDocumentPath(String vendorDocumentPath) {
        this.vendorDocumentPath = vendorDocumentPath;
        return this;
    }

    public String getVendorDocumentPath() {
        return this.vendorDocumentPath;
    }

    public void setVendorDocumentPath(String vendorDocumentPath) {
        this.vendorDocumentPath = vendorDocumentPath;
    }

    public OSellerProductRequiredDocument templateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public OSellerProductRequiredDocument documentPath(String documentPath) {
        this.documentPath = documentPath;
        return this;
    }

    public String getDocumentPath() {
        return this.documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OSellerProductRequiredDocument osellerProductRequiredDocument = (OSellerProductRequiredDocument)o;
            return Objects.equals(this.vendorDocumentPath, osellerProductRequiredDocument.vendorDocumentPath) && Objects.equals(this.templateName, osellerProductRequiredDocument.templateName) && Objects.equals(this.documentPath, osellerProductRequiredDocument.documentPath);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.vendorDocumentPath, this.templateName, this.documentPath});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OSellerProductRequiredDocument {\n");
        sb.append("    vendorDocumentPath: ").append(this.toIndentedString(this.vendorDocumentPath)).append("\n");
        sb.append("    templateName: ").append(this.toIndentedString(this.templateName)).append("\n");
        sb.append("    documentPath: ").append(this.toIndentedString(this.documentPath)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
