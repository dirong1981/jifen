package com.gljr.jifen.pojo;

import java.util.Date;

public class StoreCouponOrder {
    private Integer id;

    private Integer siId;

    private Integer ucId;

    private Integer trxId;

    private String trxCode;

    private String dtchainBlockId;

    private Integer integral;

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

    public Integer getUcId() {
        return ucId;
    }

    public void setUcId(Integer ucId) {
        this.ucId = ucId;
    }

    public Integer getTrxId() {
        return trxId;
    }

    public void setTrxId(Integer trxId) {
        this.trxId = trxId;
    }

    public String getTrxCode() {
        return trxCode;
    }

    public void setTrxCode(String trxCode) {
        this.trxCode = trxCode == null ? null : trxCode.trim();
    }

    public String getDtchainBlockId() {
        return dtchainBlockId;
    }

    public void setDtchainBlockId(String dtchainBlockId) {
        this.dtchainBlockId = dtchainBlockId == null ? null : dtchainBlockId.trim();
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}