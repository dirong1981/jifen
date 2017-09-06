package com.gljr.jifen.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
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

    private String unit;

    private Byte status;

    private Date createTime;

    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotBlank
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

    @NotNull
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @NotNull
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

    @NotNull
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

    @NotNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
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

    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}