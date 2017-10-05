package com.gljr.jifen.pojo;

import java.util.Date;

public class UserCredits {
    private Integer id;

    private Integer ownerType;

    private Integer ownerId;

    private String walletAddress;

    private Integer integral;

    private Integer frozenIntegral;

    private Integer feePaymentLimit;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress == null ? null : walletAddress.trim();
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getFrozenIntegral() {
        return frozenIntegral;
    }

    public void setFrozenIntegral(Integer frozenIntegral) {
        this.frozenIntegral = frozenIntegral;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getFeePaymentLimit() {
        return feePaymentLimit;
    }

    public void setFeePaymentLimit(Integer feePaymentLimit) {
        this.feePaymentLimit = feePaymentLimit;
    }
}