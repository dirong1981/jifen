package com.gljr.jifen.common.dtchain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class IntegralIssuedHistory implements Serializable {

    @JsonProperty(value = "block_id")
    private String blockId;

    private Long amount;

    @JsonProperty(value = "issued_time")
    private String issuedTime;

    private String operator;

    public IntegralIssuedHistory() {

    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getIssuedTime() {
        return issuedTime;
    }

    public void setIssuedTime(String issuedTime) {
        this.issuedTime = issuedTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "IntegralIssuedHistory{" +
                "blockId='" + blockId + '\'' +
                ", amount=" + amount +
                ", issuedTime=" + issuedTime +
                ", operator='" + operator + '\'' +
                '}';
    }
}
