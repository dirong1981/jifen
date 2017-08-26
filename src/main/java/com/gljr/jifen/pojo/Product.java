package com.gljr.jifen.pojo;

import java.util.Date;

public class Product {
    private String pId;

    private String pName;

    private String bcId;

    private String scId;

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

    private Integer pCreator;

    private String pIntro;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId == null ? null : pId.trim();
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    public String getBcId() {
        return bcId;
    }

    public void setBcId(String bcId) {
        this.bcId = bcId == null ? null : bcId.trim();
    }

    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId == null ? null : scId.trim();
    }

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

    public Integer getpPrice() {
        return pPrice;
    }

    public void setpPrice(Integer pPrice) {
        this.pPrice = pPrice;
    }

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

    public String getpUnit() {
        return pUnit;
    }

    public void setpUnit(String pUnit) {
        this.pUnit = pUnit == null ? null : pUnit.trim();
    }

    public Integer getpState() {
        return pState;
    }

    public void setpState(Integer pState) {
        this.pState = pState;
    }

    public Integer getpCreator() {
        return pCreator;
    }

    public void setpCreator(Integer pCreator) {
        this.pCreator = pCreator;
    }

    public String getpIntro() {
        return pIntro;
    }

    public void setpIntro(String pIntro) {
        this.pIntro = pIntro == null ? null : pIntro.trim();
    }
}