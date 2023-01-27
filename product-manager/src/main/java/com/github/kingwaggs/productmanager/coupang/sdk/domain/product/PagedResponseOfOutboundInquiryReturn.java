package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PagedResponseOfOutboundInquiryReturn {
    @SerializedName("content")
    private List<OutboundInquiryReturn> content = new ArrayList();
    @SerializedName("pagination")
    private Pagination pagination = null;

    public PagedResponseOfOutboundInquiryReturn() {
    }

    public PagedResponseOfOutboundInquiryReturn content(List<OutboundInquiryReturn> content) {
        this.content = content;
        return this;
    }

    public PagedResponseOfOutboundInquiryReturn addContentItem(OutboundInquiryReturn contentItem) {
        this.content.add(contentItem);
        return this;
    }

    public List<OutboundInquiryReturn> getContent() {
        return this.content;
    }

    public void setContent(List<OutboundInquiryReturn> content) {
        this.content = content;
    }

    public PagedResponseOfOutboundInquiryReturn pagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    public Pagination getPagination() {
        return this.pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PagedResponseOfOutboundInquiryReturn pagedResponseOfOutboundInquiryReturn = (PagedResponseOfOutboundInquiryReturn)o;
            return Objects.equals(this.content, pagedResponseOfOutboundInquiryReturn.content) && Objects.equals(this.pagination, pagedResponseOfOutboundInquiryReturn.pagination);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.content, this.pagination});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PagedResponseOfOutboundInquiryReturn {\n");
        sb.append("    content: ").append(this.toIndentedString(this.content)).append("\n");
        sb.append("    pagination: ").append(this.toIndentedString(this.pagination)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
