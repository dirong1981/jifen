package com.gljr.jifen.common.dtchain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SettlePeriodStat implements Serializable {

    private String currentFrom;

    private String currentTo;

    private Integer currentCycle;

    private Long integral;

    private String blockId;

    public SettlePeriodStat() {
        this.integral = 0L;
        this.blockId = null;
    }

    public String getCurrentFrom() {
        return currentFrom;
    }

    public void setCurrentFrom(String currentFrom) {
        this.currentFrom = currentFrom;
    }

    public String getCurrentTo() {
        return currentTo;
    }

    public void setCurrentTo(String currentTo) {
        this.currentTo = currentTo;
    }

    public Integer getCurrentCycle() {
        return currentCycle;
    }

    public void setCurrentCycle(Integer currentCycle) {
        this.currentCycle = currentCycle;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    @Override
    public String toString() {
        return "SettlePeriodStat{" +
                "currentFrom='" + currentFrom + '\'' +
                ", currentTo='" + currentTo + '\'' +
                ", currentCycle=" + currentCycle +
                ", integral=" + integral +
                ", blockId='" + blockId + '\'' +
                '}';
    }
}
