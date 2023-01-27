package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ONoticeCategory {
    @SerializedName("noticeCategoryName")
    private String noticeCategoryName = null;
    @SerializedName("noticeCategoryDetailNames")
    private List<ONoticeCategoryTemplate> noticeCategoryDetailNames = new ArrayList();

    public ONoticeCategory() {
    }

    public ONoticeCategory noticeCategoryName(String noticeCategoryName) {
        this.noticeCategoryName = noticeCategoryName;
        return this;
    }

    public String getNoticeCategoryName() {
        return this.noticeCategoryName;
    }

    public void setNoticeCategoryName(String noticeCategoryName) {
        this.noticeCategoryName = noticeCategoryName;
    }

    public ONoticeCategory noticeCategoryDetailNames(List<ONoticeCategoryTemplate> noticeCategoryDetailNames) {
        this.noticeCategoryDetailNames = noticeCategoryDetailNames;
        return this;
    }

    public ONoticeCategory addNoticeCategoryDetailNamesItem(ONoticeCategoryTemplate noticeCategoryDetailNamesItem) {
        this.noticeCategoryDetailNames.add(noticeCategoryDetailNamesItem);
        return this;
    }

    public List<ONoticeCategoryTemplate> getNoticeCategoryDetailNames() {
        return this.noticeCategoryDetailNames;
    }

    public void setNoticeCategoryDetailNames(List<ONoticeCategoryTemplate> noticeCategoryDetailNames) {
        this.noticeCategoryDetailNames = noticeCategoryDetailNames;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ONoticeCategory onoticeCategory = (ONoticeCategory)o;
            return Objects.equals(this.noticeCategoryName, onoticeCategory.noticeCategoryName) && Objects.equals(this.noticeCategoryDetailNames, onoticeCategory.noticeCategoryDetailNames);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.noticeCategoryName, this.noticeCategoryDetailNames});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ONoticeCategory {\n");
        sb.append("    noticeCategoryName: ").append(this.toIndentedString(this.noticeCategoryName)).append("\n");
        sb.append("    noticeCategoryDetailNames: ").append(this.toIndentedString(this.noticeCategoryDetailNames)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
