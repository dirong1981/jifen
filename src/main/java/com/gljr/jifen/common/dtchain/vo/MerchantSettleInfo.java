package com.gljr.jifen.common.dtchain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerchantSettleInfo implements Serializable {

    private Long id;

    @SerializedName(value = "serial_code")
    private String serialCode;

    @SerializedName(value = "store_type")
    private Integer storeType;

    private String name;

    @SerializedName(value = "manager_name")
    private String managerName;

    @SerializedName(value = "manager_tel")
    private String managerTel;

    @SerializedName(value = "bank_no")
    private String bankNo;

    @SerializedName(value = "bank_name")
    private String bankName;

    @SerializedName(value = "total_integral")
    private Long totalIntegral;

    @SerializedName(value = "last_from")
    private String lastFrom;

    @SerializedName(value = "last_to")
    private String lastTo;

    @SerializedName(value = "current_from")
    private String currentFrom;

    @SerializedName(value = "current_amount")
    private Long currentAmount;

    @SerializedName(value = "last_amount")
    private Long lastAmount;

    public MerchantSettleInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public Long getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(Long totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public String getLastFrom() {
        return lastFrom;
    }

    public void setLastFrom(String lastFrom) {
        this.lastFrom = lastFrom;
    }

    public String getLastTo() {
        return lastTo;
    }

    public void setLastTo(String lastTo) {
        this.lastTo = lastTo;
    }

    public String getCurrentFrom() {
        return currentFrom;
    }

    public void setCurrentFrom(String currentFrom) {
        this.currentFrom = currentFrom;
    }

    public Long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Long getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(Long lastAmount) {
        this.lastAmount = lastAmount;
    }

    public String getManagerTel() {
        return managerTel;
    }

    public void setManagerTel(String managerTel) {
        this.managerTel = managerTel;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "MerchantSettleInfo{" +
                "id=" + id +
                ", serialCode='" + serialCode + '\'' +
                ", storeType=" + storeType +
                ", name='" + name + '\'' +
                ", managerName='" + managerName + '\'' +
                ", managerTel='" + managerTel + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", totalIntegral=" + totalIntegral +
                ", lastFrom='" + lastFrom + '\'' +
                ", lastTo='" + lastTo + '\'' +
                ", currentFrom='" + currentFrom + '\'' +
                ", currentAmount=" + currentAmount +
                ", lastAmount=" + lastAmount +
                '}';
    }
}
