package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Pagination {
    @SerializedName("countPerPage")
    private Integer countPerPage = null;
    @SerializedName("currentPage")
    private Integer currentPage = null;
    @SerializedName("totalElements")
    private Integer totalElements = null;
    @SerializedName("totalPages")
    private Integer totalPages = null;

    public Pagination() {
    }

    public Pagination countPerPage(Integer countPerPage) {
        this.countPerPage = countPerPage;
        return this;
    }

    public Integer getCountPerPage() {
        return this.countPerPage;
    }

    public void setCountPerPage(Integer countPerPage) {
        this.countPerPage = countPerPage;
    }

    public Pagination currentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Pagination totalElements(Integer totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public Integer getTotalElements() {
        return this.totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Pagination totalPages(Integer totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Pagination pagination = (Pagination)o;
            return Objects.equals(this.countPerPage, pagination.countPerPage) && Objects.equals(this.currentPage, pagination.currentPage) && Objects.equals(this.totalElements, pagination.totalElements) && Objects.equals(this.totalPages, pagination.totalPages);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.countPerPage, this.currentPage, this.totalElements, this.totalPages});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Pagination {\n");
        sb.append("    countPerPage: ").append(this.toIndentedString(this.countPerPage)).append("\n");
        sb.append("    currentPage: ").append(this.toIndentedString(this.currentPage)).append("\n");
        sb.append("    totalElements: ").append(this.toIndentedString(this.totalElements)).append("\n");
        sb.append("    totalPages: ").append(this.toIndentedString(this.totalPages)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
