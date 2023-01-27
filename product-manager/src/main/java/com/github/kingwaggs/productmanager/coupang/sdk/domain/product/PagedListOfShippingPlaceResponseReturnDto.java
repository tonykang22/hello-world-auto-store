package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PagedListOfShippingPlaceResponseReturnDto {
    @SerializedName("content")
    private List<ShippingPlaceResponseReturnDto> content = new ArrayList();
    @SerializedName("pagination")
    private Pagination pagination = null;

    public PagedListOfShippingPlaceResponseReturnDto() {
    }

    public PagedListOfShippingPlaceResponseReturnDto content(List<ShippingPlaceResponseReturnDto> content) {
        this.content = content;
        return this;
    }

    public PagedListOfShippingPlaceResponseReturnDto addContentItem(ShippingPlaceResponseReturnDto contentItem) {
        this.content.add(contentItem);
        return this;
    }

    public List<ShippingPlaceResponseReturnDto> getContent() {
        return this.content;
    }

    public void setContent(List<ShippingPlaceResponseReturnDto> content) {
        this.content = content;
    }

    public PagedListOfShippingPlaceResponseReturnDto pagination(Pagination pagination) {
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
            PagedListOfShippingPlaceResponseReturnDto pagedListOfShippingPlaceResponseReturnDto = (PagedListOfShippingPlaceResponseReturnDto)o;
            return Objects.equals(this.content, pagedListOfShippingPlaceResponseReturnDto.content) && Objects.equals(this.pagination, pagedListOfShippingPlaceResponseReturnDto.pagination);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.content, this.pagination});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PagedListOfShippingPlaceResponseReturnDto {\n");
        sb.append("    content: ").append(this.toIndentedString(this.content)).append("\n");
        sb.append("    pagination: ").append(this.toIndentedString(this.pagination)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
