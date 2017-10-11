package com.gljr.jifen.pojo;

import java.io.Serializable;
import java.util.Date;

public class OrderRefund implements Serializable{

    private Long id;

    private Long orderId;

    private Integer orderType;

    private Long trxId;

    private String trxCode;

    private String dtchainBlockId;

    private String extOrderId;

    private Long storeId;

    private Long toUid;

    private Long integral;

    private Date created;

    public OrderRefund() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getTrxId() {
        return trxId;
    }

    public void setTrxId(Long trxId) {
        this.trxId = trxId;
    }

    public String getTrxCode() {
        return trxCode;
    }

    public void setTrxCode(String trxCode) {
        this.trxCode = trxCode;
    }

    public String getDtchainBlockId() {
        return dtchainBlockId;
    }

    public void setDtchainBlockId(String dtchainBlockId) {
        this.dtchainBlockId = dtchainBlockId;
    }

    public String getExtOrderId() {
        return extOrderId;
    }

    public void setExtOrderId(String extOrderId) {
        this.extOrderId = extOrderId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getToUid() {
        return toUid;
    }

    public void setToUid(Long toUid) {
        this.toUid = toUid;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
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
