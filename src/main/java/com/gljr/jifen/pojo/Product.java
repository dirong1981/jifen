package com.gljr.jifen.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Product {
    private String pId;

    private String pName;

    private String cpId;

    private String cId;

    private String sId;

    private Date pTime;

    private Integer pPrice;

    private Integer pIntegral;

    private Integer pDiscounts;

    private String pLogo;

    private Integer pStorage;

    private Integer pSales;

    private String pUnit;

    private Integer pState;

    private String pCreator;

    private String pIntro;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId == null ? null : pId.trim();
    }

    @NotBlank
    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    @NotBlank
    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId == null ? null : cpId.trim();
    }

    @NotBlank
    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId == null ? null : cId.trim();
    }

    @NotBlank
    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId == null ? null : sId.trim();
    }

    public Date getpTime() {
        return pTime;
    }

    public void setpTime(Date pTime) {
        this.pTime = pTime;
    }

    @NotNull
    public Integer getpPrice() {
        return pPrice;
    }

    public void setpPrice(Integer pPrice) {
        this.pPrice = pPrice;
    }

    @NotNull
    public Integer getpIntegral() {
        return pIntegral;
    }

    public void setpIntegral(Integer pIntegral) {
        this.pIntegral = pIntegral;
    }

    public Integer getpDiscounts() {
        return pDiscounts;
    }

    public void setpDiscounts(Integer pDiscounts) {
        this.pDiscounts = pDiscounts;
    }


    public String getpLogo() {
        return pLogo;
    }

    public void setpLogo(String pLogo) {
        this.pLogo = pLogo == null ? null : pLogo.trim();
    }

    @NotNull
    public Integer getpStorage() {
        return pStorage;
    }

    public void setpStorage(Integer pStorage) {
        this.pStorage = pStorage;
    }

    public Integer getpSales() {
        return pSales;
    }

    public void setpSales(Integer pSales) {
        this.pSales = pSales;
    }

    @NotBlank
    public String getpUnit() {
        return pUnit;
    }

    public void setpUnit(String pUnit) {
        this.pUnit = pUnit == null ? null : pUnit.trim();
    }

    @NotNull
    public Integer getpState() {
        return pState;
    }

    public void setpState(Integer pState) {
        this.pState = pState;
    }

    @NotBlank
    public String getpCreator() {
        return pCreator;
    }

    public void setpCreator(String pCreator) {
        this.pCreator = pCreator == null ? null : pCreator.trim();
    }

    @NotBlank
    public String getpIntro() {
        return pIntro;
    }

    public void setpIntro(String pIntro) {
        this.pIntro = pIntro == null ? null : pIntro.trim();
    }
}