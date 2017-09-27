package com.gljr.jifen.pojo;

import java.util.Date;

public class ModuleAggregationCondition {
    private Integer id;

    private Integer aggregationId;

    private Date createTime;

    private String expStr;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExpStr() {
        return expStr;
    }

    public void setExpStr(String expStr) {
        this.expStr = expStr == null ? null : expStr.trim();
    }
}