package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OCategoryMeta {
    @SerializedName("allowedOfferConditions")
    private List<String> allowedOfferConditions = new ArrayList();
    @SerializedName("isAllowSingleItem")
    private Boolean isAllowSingleItem = null;
    @SerializedName("requiredDocumentNames")
    private List<ODocumentTemplate> requiredDocumentNames = new ArrayList();
    @SerializedName("noticeCategories")
    private List<ONoticeCategory> noticeCategories = new ArrayList();
    @SerializedName("certifications")
    private List<OCertification> certifications = new ArrayList();
    @SerializedName("attributes")
    private List<OAttributeType> attributes = new ArrayList();

    public OCategoryMeta() {
    }

    public OCategoryMeta allowedOfferConditions(List<String> allowedOfferConditions) {
        this.allowedOfferConditions = allowedOfferConditions;
        return this;
    }

    public OCategoryMeta addAllowedOfferConditionsItem(String allowedOfferConditionsItem) {
        this.allowedOfferConditions.add(allowedOfferConditionsItem);
        return this;
    }

    public List<String> getAllowedOfferConditions() {
        return this.allowedOfferConditions;
    }

    public void setAllowedOfferConditions(List<String> allowedOfferConditions) {
        this.allowedOfferConditions = allowedOfferConditions;
    }

    public OCategoryMeta isAllowSingleItem(Boolean isAllowSingleItem) {
        this.isAllowSingleItem = isAllowSingleItem;
        return this;
    }

    public Boolean getIsAllowSingleItem() {
        return this.isAllowSingleItem;
    }

    public void setIsAllowSingleItem(Boolean isAllowSingleItem) {
        this.isAllowSingleItem = isAllowSingleItem;
    }

    public OCategoryMeta requiredDocumentNames(List<ODocumentTemplate> requiredDocumentNames) {
        this.requiredDocumentNames = requiredDocumentNames;
        return this;
    }

    public OCategoryMeta addRequiredDocumentNamesItem(ODocumentTemplate requiredDocumentNamesItem) {
        this.requiredDocumentNames.add(requiredDocumentNamesItem);
        return this;
    }

    public List<ODocumentTemplate> getRequiredDocumentNames() {
        return this.requiredDocumentNames;
    }

    public void setRequiredDocumentNames(List<ODocumentTemplate> requiredDocumentNames) {
        this.requiredDocumentNames = requiredDocumentNames;
    }

    public OCategoryMeta noticeCategories(List<ONoticeCategory> noticeCategories) {
        this.noticeCategories = noticeCategories;
        return this;
    }

    public OCategoryMeta addNoticeCategoriesItem(ONoticeCategory noticeCategoriesItem) {
        this.noticeCategories.add(noticeCategoriesItem);
        return this;
    }

    public List<ONoticeCategory> getNoticeCategories() {
        return this.noticeCategories;
    }

    public void setNoticeCategories(List<ONoticeCategory> noticeCategories) {
        this.noticeCategories = noticeCategories;
    }

    public OCategoryMeta certifications(List<OCertification> certifications) {
        this.certifications = certifications;
        return this;
    }

    public OCategoryMeta addCertificationsItem(OCertification certificationsItem) {
        this.certifications.add(certificationsItem);
        return this;
    }

    public List<OCertification> getCertifications() {
        return this.certifications;
    }

    public void setCertifications(List<OCertification> certifications) {
        this.certifications = certifications;
    }

    public OCategoryMeta attributes(List<OAttributeType> attributes) {
        this.attributes = attributes;
        return this;
    }

    public OCategoryMeta addAttributesItem(OAttributeType attributesItem) {
        this.attributes.add(attributesItem);
        return this;
    }

    public List<OAttributeType> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(List<OAttributeType> attributes) {
        this.attributes = attributes;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OCategoryMeta ocategoryMeta = (OCategoryMeta)o;
            return Objects.equals(this.allowedOfferConditions, ocategoryMeta.allowedOfferConditions) && Objects.equals(this.isAllowSingleItem, ocategoryMeta.isAllowSingleItem) && Objects.equals(this.requiredDocumentNames, ocategoryMeta.requiredDocumentNames) && Objects.equals(this.noticeCategories, ocategoryMeta.noticeCategories) && Objects.equals(this.certifications, ocategoryMeta.certifications) && Objects.equals(this.attributes, ocategoryMeta.attributes);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.allowedOfferConditions, this.isAllowSingleItem, this.requiredDocumentNames, this.noticeCategories, this.certifications, this.attributes});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OCategoryMeta {\n");
        sb.append("    allowedOfferConditions: ").append(this.toIndentedString(this.allowedOfferConditions)).append("\n");
        sb.append("    isAllowSingleItem: ").append(this.toIndentedString(this.isAllowSingleItem)).append("\n");
        sb.append("    requiredDocumentNames: ").append(this.toIndentedString(this.requiredDocumentNames)).append("\n");
        sb.append("    noticeCategories: ").append(this.toIndentedString(this.noticeCategories)).append("\n");
        sb.append("    certifications: ").append(this.toIndentedString(this.certifications)).append("\n");
        sb.append("    attributes: ").append(this.toIndentedString(this.attributes)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
