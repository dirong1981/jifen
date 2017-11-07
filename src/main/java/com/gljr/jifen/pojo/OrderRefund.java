package com.gljr.jifen.pojo;

import java.util.Date;

public class OrderRefund {
    private Integer id;

    private Integer orderId;

    private Integer orderType;

    private Integer trxId;

    private String trxCode;

    private String dtchainBlockId;

    private String extOrderId;

    private Integer storeId;

    private Integer toUid;

    private Integer integral;

    private Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
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

    public String getExtOrderId() {
        return extOrderId;
    }

    public void setExtOrderId(String extOrderId) {
        this.extOrderId = extOrderId == null ? null : extOrderId.trim();
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getToUid() {
        return toUid;
    }

    public void setToUid(Integer toUid) {
        this.toUid = toUid;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "OrderRefund{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", orderType=" + orderType +
                ", trxId=" + trxId +
                ", trxCode='" + trxCode + '\'' +
                ", dtchainBlockId='" + dtchainBlockId + '\'' +
                ", extOrderId='" + extOrderId + '\'' +
                ", storeId=" + storeId +
                ", toUid=" + toUid +
                ", integral=" + integral +
                ", created=" + created +
                '}';
    }
}