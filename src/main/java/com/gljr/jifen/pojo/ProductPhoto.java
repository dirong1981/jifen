package com.gljr.jifen.pojo;

import java.util.Date;

public class ProductPhoto {
    private Integer id;

    private Integer pid;

    private String imgKey;

    private Integer sort;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey == null ? null : imgKey.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}