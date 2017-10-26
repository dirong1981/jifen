package com.gljr.jifen.pojo;

import java.util.Date;

public class StoreExtInfo {
    private Integer id;

    private Integer siId;

    private String licenseNo;

    private String licenseFileKey;

    private String principalName;

    private String principalIdNo;

    private String principalPhone;

    private String bankAccount;

    private String bankName;

    private String bankAccountName;

    private Integer settleCycle;

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

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo == null ? null : licenseNo.trim();
    }

    public String getLicenseFileKey() {
        return licenseFileKey;
    }

    public void setLicenseFileKey(String licenseFileKey) {
        this.licenseFileKey = licenseFileKey == null ? null : licenseFileKey.trim();
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName == null ? null : principalName.trim();
    }

    public String getPrincipalIdNo() {
        return principalIdNo;
    }

    public void setPrincipalIdNo(String principalIdNo) {
        this.principalIdNo = principalIdNo == null ? null : principalIdNo.trim();
    }

    public String getPrincipalPhone() {
        return principalPhone;
    }

    public void setPrincipalPhone(String principalPhone) {
        this.principalPhone = principalPhone == null ? null : principalPhone.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName == null ? null : bankAccountName.trim();
    }

    public Integer getSettleCycle() {
        return settleCycle;
    }

    public void setSettleCycle(Integer settleCycle) {
        this.settleCycle = settleCycle;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}