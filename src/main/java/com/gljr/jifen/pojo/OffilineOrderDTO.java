package com.gljr.jifen.pojo;

/**
 * 线下订单数据传输对象
 *
 * Created by Administrator on 2017/9/28 0028.
 */
public class OffilineOrderDTO {
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 此订单消费积分
     */
    private int integral;
    /**
     * 此订单消费现金
     */
    private int cash;
    /**
     * 订单状态（1：待付款；2：已付款；3：已退款；4：已取消）
     */
    private int orderStat;
    /**
     * 订单日期，格式为yyyy/MM/dd HH:mm
     */
    private String orderDate;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getOrderStat() {
        return orderStat;
    }

    public void setOrderStat(int orderStat) {
        this.orderStat = orderStat;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
