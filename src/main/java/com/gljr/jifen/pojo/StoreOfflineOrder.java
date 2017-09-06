package com.gljr.jifen.pojo;

import java.util.Date;

public class StoreOfflineOrder {
    private Integer id;

    private Integer siId;

    private Integer uid;

    private Integer trxId;

    private String trxCode;

    private Integer totalMoney;

    private Integer integral;

    private Integer extCash;

    private Byte status;

    private Date createTime;

    private Date updateTime;

    private String oContext;

    private String createTimeText;

    private String uName;


    public String getoContext() {
        return oContext;
    }

    public void setoContext(String oContext) {
        this.oContext = oContext;
    }

    public String getCreateTimeText() {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getExtCash() {
        return extCash;
    }

    public void setExtCash(Integer extCash) {
        this.extCash = extCash;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}