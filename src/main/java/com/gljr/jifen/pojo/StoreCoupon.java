package com.gljr.jifen.pojo;

import java.util.Date;

public class StoreCoupon {
    private Integer id;

    private Integer siId;

    private String storeName;

    private Integer maxGenerated;

    private Integer remainingAmount;

    private Integer minConsumption;

    private Integer integral;

    private Integer equalMoney;

    private Integer validityType;

    private Date validFrom;

    private Date validTo;

    private Integer validDays;

    private Integer allowCancel;

    private Integer status;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiId() {
        return siId;
    }

    public void setSiId(Integer siId) {
        this.siId = siId;
    }

    public Integer getMaxGenerated() {
        return maxGenerated;
    }

    public void setMaxGenerated(Integer maxGenerated) {
        this.maxGenerated = maxGenerated;
    }

    public Integer getMinConsumption() {
        return minConsumption;
    }

    public void setMinConsumption(Integer minConsumption) {
        this.minConsumption = minConsumption;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getEqualMoney() {
        return equalMoney;
    }

    public void setEqualMoney(Integer equalMoney) {
        this.equalMoney = equalMoney;
    }

    public Integer getValidityType() {
        return validityType;
    }

    public void setValidityType(Integer validityType) {
        this.validityType = validityType;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Integer getValidDays() {
        return validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }

    public Integer getAllowCancel() {
        return allowCancel;
    }

    public void setAllowCancel(Integer allowCancel) {
        this.allowCancel = allowCancel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
}