package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AutoCategorizationResponseDto {
    @SerializedName("autoCategorizationPredictionResultType")
    private AutoCategorizationResponseDto.AutoCategorizationPredictionResultTypeEnum autoCategorizationPredictionResultType = null;
    @SerializedName("comment")
    private String comment = null;
    @SerializedName("predictedCategoryId")
    private String predictedCategoryId = null;
    @SerializedName("predictedCategoryName")
    private String predictedCategoryName = null;

    public AutoCategorizationResponseDto() {
    }

    public AutoCategorizationResponseDto autoCategorizationPredictionResultType(AutoCategorizationResponseDto.AutoCategorizationPredictionResultTypeEnum autoCategorizationPredictionResultType) {
        this.autoCategorizationPredictionResultType = autoCategorizationPredictionResultType;
        return this;
    }

    public AutoCategorizationResponseDto.AutoCategorizationPredictionResultTypeEnum getAutoCategorizationPredictionResultType() {
        return this.autoCategorizationPredictionResultType;
    }

    public void setAutoCategorizationPredictionResultType(AutoCategorizationResponseDto.AutoCategorizationPredictionResultTypeEnum autoCategorizationPredictionResultType) {
        this.autoCategorizationPredictionResultType = autoCategorizationPredictionResultType;
    }

    public AutoCategorizationResponseDto comment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AutoCategorizationResponseDto predictedCategoryId(String predictedCategoryId) {
        this.predictedCategoryId = predictedCategoryId;
        return this;
    }

    public String getPredictedCategoryId() {
        return this.predictedCategoryId;
    }

    public void setPredictedCategoryId(String predictedCategoryId) {
        this.predictedCategoryId = predictedCategoryId;
    }

    public AutoCategorizationResponseDto predictedCategoryName(String predictedCategoryName) {
        this.predictedCategoryName = predictedCategoryName;
        return this;
    }

    public String getPredictedCategoryName() {
        return this.predictedCategoryName;
    }

    public void setPredictedCategoryName(String predictedCategoryName) {
        this.predictedCategoryName = predictedCategoryName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AutoCategorizationResponseDto autoCategorizationResponseDto = (AutoCategorizationResponseDto)o;
            return Objects.equals(this.autoCategorizationPredictionResultType, autoCategorizationResponseDto.autoCategorizationPredictionResultType) && Objects.equals(this.comment, autoCategorizationResponseDto.comment) && Objects.equals(this.predictedCategoryId, autoCategorizationResponseDto.predictedCategoryId) && Objects.equals(this.predictedCategoryName, autoCategorizationResponseDto.predictedCategoryName);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.autoCategorizationPredictionResultType, this.comment, this.predictedCategoryId, this.predictedCategoryName});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AutoCategorizationResponseDto {\n");
        sb.append("    autoCategorizationPredictionResultType: ").append(this.toIndentedString(this.autoCategorizationPredictionResultType)).append("\n");
        sb.append("    comment: ").append(this.toIndentedString(this.comment)).append("\n");
        sb.append("    predictedCategoryId: ").append(this.toIndentedString(this.predictedCategoryId)).append("\n");
        sb.append("    predictedCategoryName: ").append(this.toIndentedString(this.predictedCategoryName)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    public static enum AutoCategorizationPredictionResultTypeEnum {
        @SerializedName("SUCCESS")
        SUCCESS("SUCCESS"),
        @SerializedName("FAILURE")
        FAILURE("FAILURE"),
        @SerializedName("INSUFFICIENT_INFORMATION")
        INSUFFICIENT_INFORMATION("INSUFFICIENT_INFORMATION");

        private String value;

        private AutoCategorizationPredictionResultTypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }
}
