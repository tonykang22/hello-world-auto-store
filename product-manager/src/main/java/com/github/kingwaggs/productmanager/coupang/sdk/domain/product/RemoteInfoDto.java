package com.github.kingwaggs.productmanager.coupang.sdk.domain.product;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RemoteInfoDto {
    @SerializedName("deliveryCode")
    private String deliveryCode = null;
    @SerializedName("jeju")
    private Integer jeju = null;
    @SerializedName("notJeju")
    private Integer notJeju = null;
    @SerializedName("remoteInfoId")
    private Long remoteInfoId = null;
    @SerializedName("usable")
    private Boolean usable = null;

    public RemoteInfoDto() {
    }

    public RemoteInfoDto deliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
        return this;
    }

    public String getDeliveryCode() {
        return this.deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public RemoteInfoDto jeju(Integer jeju) {
        this.jeju = jeju;
        return this;
    }

    public Integer getJeju() {
        return this.jeju;
    }

    public void setJeju(Integer jeju) {
        this.jeju = jeju;
    }

    public RemoteInfoDto notJeju(Integer notJeju) {
        this.notJeju = notJeju;
        return this;
    }

    public Integer getNotJeju() {
        return this.notJeju;
    }

    public void setNotJeju(Integer notJeju) {
        this.notJeju = notJeju;
    }

    public RemoteInfoDto remoteInfoId(Long remoteInfoId) {
        this.remoteInfoId = remoteInfoId;
        return this;
    }

    public Long getRemoteInfoId() {
        return this.remoteInfoId;
    }

    public void setRemoteInfoId(Long remoteInfoId) {
        this.remoteInfoId = remoteInfoId;
    }

    public RemoteInfoDto usable(Boolean usable) {
        this.usable = usable;
        return this;
    }

    public Boolean getUsable() {
        return this.usable;
    }

    public void setUsable(Boolean usable) {
        this.usable = usable;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RemoteInfoDto remoteInfoDto = (RemoteInfoDto)o;
            return Objects.equals(this.deliveryCode, remoteInfoDto.deliveryCode) && Objects.equals(this.jeju, remoteInfoDto.jeju) && Objects.equals(this.notJeju, remoteInfoDto.notJeju) && Objects.equals(this.remoteInfoId, remoteInfoDto.remoteInfoId) && Objects.equals(this.usable, remoteInfoDto.usable);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.deliveryCode, this.jeju, this.notJeju, this.remoteInfoId, this.usable});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RemoteInfoDto {\n");
        sb.append("    deliveryCode: ").append(this.toIndentedString(this.deliveryCode)).append("\n");
        sb.append("    jeju: ").append(this.toIndentedString(this.jeju)).append("\n");
        sb.append("    notJeju: ").append(this.toIndentedString(this.notJeju)).append("\n");
        sb.append("    remoteInfoId: ").append(this.toIndentedString(this.remoteInfoId)).append("\n");
        sb.append("    usable: ").append(this.toIndentedString(this.usable)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
