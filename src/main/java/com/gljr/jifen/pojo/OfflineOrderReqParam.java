package com.gljr.jifen.pojo;

/**
 * 商户查询交易记录请求参数类
 *
 * Created by Administrator on 2017/9/28 0028.
 */
public class OfflineOrderReqParam {
    /**
     * 查询类型
     * 0-全部；1-今天；2-最近7天；3-最近30天；4-未结算；
     * 5-按时间查询（*时间范围 *类型（全部、积分、积分+现金） *交易状态（未结算、已结算、全部））
     */
    private Integer queryType;

    /**
     * 查询起始时间
     */
    private String startTime;

    /**
     * 查询结束时间
     */
    private String endTime;

    /**
     * 积分使用类型,0-全部，默认值； 1-积分； 2-积分+现金
     */
    private Integer useType;

    /**
     * 交易状态，0-全部； 1-未结算； 2-已结算
     */
    private Integer transationStat;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer pageNum;

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
    }

    public Integer getTransationStat() {
        return transationStat;
    }

    public void setTransationStat(Integer transationStat) {
        this.transationStat = transationStat;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
