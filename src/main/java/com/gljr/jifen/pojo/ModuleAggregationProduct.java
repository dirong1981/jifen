package com.gljr.jifen.pojo;

import java.util.Date;

public class ModuleAggregationProduct {
    private Integer id;

    private Integer aggregationId;

    private Integer type;

    private Integer productId;

    private Integer storeId;

    private Integer sort;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAggregationId() {
        return aggregationId;
    }

    public void setAggregationId(Integer aggregationId) {
        this.aggregationId = aggregationId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
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