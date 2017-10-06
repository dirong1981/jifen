package com.gljr.jifen.pojo;

import java.util.Date;

public class Product {
    private Integer id;

    private String name;

    private Integer categoryCode;

    private Integer siId;

    private Integer price;

    private Integer integral;

    private Integer discounts;

    private String logoKey;

    private Integer invetory;

    private Integer sales;

    private Integer maxPurchases;

    private String unit;

    private Integer status;

    private Date createTime;

    private String description;

    private String storeName;

    private Integer userPurchases;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getSiId() {
        return siId;
    }

    public void setSiId(Integer siId) {
        this.siId = siId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Integer discounts) {
        this.discounts = discounts;
    }

    public String getLogoKey() {
        return logoKey;
    }

    public void setLogoKey(String logoKey) {
        this.logoKey = logoKey == null ? null : logoKey.trim();
    }

    public Integer getInvetory() {
        return invetory;
    }

    public void setInvetory(Integer invetory) {
        this.invetory = invetory;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getMaxPurchases() {
        return maxPurchases;
    }

    public void setMaxPurchases(Integer maxPurchases) {
        this.maxPurchases = maxPurchases;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getUserPurchases() {
        return userPurchases;
    }

    public void setUserPurchases(Integer userPurchases) {
        this.userPurchases = userPurchases;
    }
}