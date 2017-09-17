package com.gljr.jifen.pojo;

import java.util.Date;

public class StoreCoupon {
    private Integer id;

    private Integer siId;

    private Integer maxGenerated;

    private Integer integral;

    private Integer equalMoney;

    private Byte validityType;

    private Date validFrom;

    private Date validTo;

    private Integer validDays;

    private Byte status;

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

    public Byte getValidityType() {
        return validityType;
    }

    public void setValidityType(Byte validityType) {
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}