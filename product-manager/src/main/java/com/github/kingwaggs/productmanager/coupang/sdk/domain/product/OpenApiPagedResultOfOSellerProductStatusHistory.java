package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OpenApiPagedResultOfOSellerProductStatusHistory {
        @SerializedName("nextToken")
        private String nextToken = null;
        @SerializedName("message")
        private String message = null;
        @SerializedName("data")
        private List<OSellerProductStatusHistory> data = new ArrayList();
        @SerializedName("code")
        private String code = null;

        public OpenApiPagedResultOfOSellerProductStatusHistory() {
        }

        public OpenApiPagedResultOfOSellerProductStatusHistory nextToken(String nextToken) {
            this.nextToken = nextToken;
            return this;
        }

        public String getNextToken() {
            return this.nextToken;
        }

        public void setNextToken(String nextToken) {
            this.nextToken = nextToken;
        }

        public OpenApiPagedResultOfOSellerProductStatusHistory message(String message) {
            this.message = message;
            return this;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public OpenApiPagedResultOfOSellerProductStatusHistory data(List<OSellerProductStatusHistory> data) {
            this.data = data;
            return this;
        }

        public OpenApiPagedResultOfOSellerProductStatusHistory addDataItem(OSellerProductStatusHistory dataItem) {
            this.data.add(dataItem);
            return this;
        }

        public List<OSellerProductStatusHistory> getData() {
            return this.data;
        }

        public void setData(List<OSellerProductStatusHistory> data) {
            this.data = data;
        }

        public OpenApiPagedResultOfOSellerProductStatusHistory code(String code) {
            this.code = code;
            return this;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                OpenApiPagedResultOfOSellerProductStatusHistory openApiPagedResultOfOSellerProductStatusHistory = (OpenApiPagedResultOfOSellerProductStatusHistory)o;
                return Objects.equals(this.nextToken, openApiPagedResultOfOSellerProductStatusHistory.nextToken) && Objects.equals(this.message, openApiPagedResultOfOSellerProductStatusHistory.message) && Objects.equals(this.data, openApiPagedResultOfOSellerProductStatusHistory.data) && Objects.equals(this.code, openApiPagedResultOfOSellerProductStatusHistory.code);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.nextToken, this.message, this.data, this.code});
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class OpenApiPagedResultOfOSellerProductStatusHistory {\n");
            sb.append("    nextToken: ").append(this.toIndentedString(this.nextToken)).append("\n");
            sb.append("    message: ").append(this.toIndentedString(this.message)).append("\n");
            sb.append("    data: ").append(this.toIndentedString(this.data)).append("\n");
            sb.append("    code: ").append(this.toIndentedString(this.code)).append("\n");
            sb.append("}");
            return sb.toString();
        }

        private String toIndentedString(Object o) {
            return o == null ? "null" : o.toString().replace("\n", "\n    ");
        }
}
