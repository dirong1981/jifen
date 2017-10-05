package com.gljr.jifen.pojo;

import java.util.Date;
import java.util.List;

public class Module {
    private Integer id;

    private String title;

    private String description;

    private Integer type;

    private String thumbKey;

    private Integer extType;

    private Integer status;

    private Integer managerId;

    private Date createTime;

    private List picture;

    private List product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getThumbKey() {
        return thumbKey;
    }

    public void setThumbKey(String thumbKey) {
        this.thumbKey = thumbKey == null ? null : thumbKey.trim();
    }

    public Integer getExtType() {
        return extType;
    }

    public void setExtType(Integer extType) {
        this.extType = extType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List getProduct() {
        return product;
    }

    public void setProduct(List product) {
        this.product = product;
    }

    public List getPicture() {
        return picture;
    }

    public void setPicture(List picture) {
        this.picture = picture;
    }
}