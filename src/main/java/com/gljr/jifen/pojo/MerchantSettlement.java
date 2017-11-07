package com.gljr.jifen.pojo;

import java.io.Serializable;
import java.util.Date;

public class MerchantSettlement implements Serializable {

    private Long id;

    private String settlementNo;

    private Long merchantId;

    private String lastBlockId;

    private String currentFrom;

    private String currentTo;

    private Long currentAmount;

    private Date settleTime;

    private Long totalAmount;

    private Integer settleType;

    private String bankAccount;

    private Integer settleCycle;

    private String remitDate;

    private String bankReceiptKey;

    private Integer status;

    private Long managerId;

    private Date updateTime;

    public MerchantSettlement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getLastBlockId() {
        return lastBlockId;
    }

    public void setLastBlockId(String lastBlockId) {
        this.lastBlockId = lastBlockId;
    }

    public String getCurrentFrom() {
        return currentFrom;
    }

    public void setCurrentFrom(String currentFrom) {
        this.currentFrom = currentFrom;
    }

    public String getCurrentTo() {
        return currentTo;
    }

    public void setCurrentTo(String currentTo) {
        this.currentTo = currentTo;
    }

    public Long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Date getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getSettleType() {
        return settleType;
    }

    public void setSettleType(Integer settleType) {
        this.settleType = settleType;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getSettleCycle() {
        return settleCycle;
    }

    public void setSettleCycle(Integer settleCycle) {
        this.settleCycle = settleCycle;
    }

    public String getRemitDate() {
        return remitDate;
    }

    public void setRemitDate(String remitDate) {
        this.remitDate = remitDate;
    }

    public String getBankReceiptKey() {
        return bankReceiptKey;
    }

    public void setBankReceiptKey(String bankReceiptKey) {
        this.bankReceiptKey = bankReceiptKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MerchantSettlement{" +
                "id=" + id +
                ", settlementNo='" + settlementNo + '\'' +
                ", merchantId=" + merchantId +
                ", lastBlockId='" + lastBlockId + '\'' +
                ", currentFrom='" + currentFrom + '\'' +
                ", currentTo='" + currentTo + '\'' +
                ", currentAmount=" + currentAmount +
                ", settleTime=" + settleTime +
                ", totalAmount=" + totalAmount +
                ", settleType=" + settleType +
                ", bankAccount='" + bankAccount + '\'' +
                ", settleCycle=" + settleCycle +
                ", remitDate='" + remitDate + '\'' +
                ", bankReceiptKey='" + bankReceiptKey + '\'' +
                ", status=" + status +
                ", managerId=" + managerId +
                ", updateTime=" + updateTime +
                '}';
    }
}
