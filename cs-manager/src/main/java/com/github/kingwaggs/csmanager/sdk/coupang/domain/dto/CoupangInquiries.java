package com.github.kingwaggs.csmanager.sdk.coupang.domain.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class CoupangInquiries {

    private Integer code;
    @SerializedName("data")
    private PaginatedInquiries paginatedInquiries;
    private String message;

    @Data
    public static class PaginatedInquiries {
        @SerializedName("content")
        private List<Inquiry> inquiryList;
        private Pagination pagination;
    }

    @Data
    public static class Inquiry {
        private String buyerEmail;
        @SerializedName("commentDtoList")
        private List<InquiryAnswer> answerList;
        private String content;
        private String inquiryAt;
        private Long inquiryId;
        private List<Long> orderIds;
        private Long productId;
        @SerializedName("sellerItemId")
        private Long sellerItemId;
        @SerializedName("sellerProductId")
        private Long sellerProductId;
        @SerializedName("vendorItemId")
        private Long vendorItemId;
    }

    @Data
    public static class InquiryAnswer {
        private String content;
        @SerializedName("inquiryCommentAt")
        private String answeredAt;
        @SerializedName("inquiryCommentId")
        private Long inquiryAnswerId;
        private Long inquiryId;
    }

    @Data
    public static class Pagination {
        private Integer countPerPage;
        private Integer currentPage;
        private Integer totalElements;
        private Integer totalPages;
    }

    public List<Inquiry> getInquiryList() {
        return getPaginatedInquiries().getInquiryList();
    }

}
