package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class OSellerProductStatusHistory {
    @SerializedName("statusName")
    private String statusName = null;
    @SerializedName("createdBy")
    private String createdBy = null;
    @SerializedName("createdAt")
    private String createdAt = null;
    @SerializedName("comment")
    private String comment = null;

    public OSellerProductStatusHistory() {
    }

    public OSellerProductStatusHistory statusName(String statusName) {
        this.statusName = statusName;
        return this;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public OSellerProductStatusHistory createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OSellerProductStatusHistory createdAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public OSellerProductStatusHistory comment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            OSellerProductStatusHistory osellerProductStatusHistory = (OSellerProductStatusHistory)o;
            return Objects.equals(this.statusName, osellerProductStatusHistory.statusName) && Objects.equals(this.createdBy, osellerProductStatusHistory.createdBy) && Objects.equals(this.createdAt, osellerProductStatusHistory.createdAt) && Objects.equals(this.comment, osellerProductStatusHistory.comment);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.statusName, this.createdBy, this.createdAt, this.comment});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OSellerProductStatusHistory {\n");
        sb.append("    statusName: ").append(this.toIndentedString(this.statusName)).append("\n");
        sb.append("    createdBy: ").append(this.toIndentedString(this.createdBy)).append("\n");
        sb.append("    createdAt: ").append(this.toIndentedString(this.createdAt)).append("\n");
        sb.append("    comment: ").append(this.toIndentedString(this.comment)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
