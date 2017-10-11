package com.gljr.jifen.common.dtchain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class IntegralStatInfo implements Serializable{

    private Long issued = 0L;

    private Long held = 0L;

    private Long circulated = 0L;

    private Long recycled = 0L;

    public IntegralStatInfo() {

    }

    public Long getIssued() {
        return issued;
    }

    public void setIssued(Long issued) {
        this.issued = issued;
    }

    public Long getHeld() {
        return held;
    }

    public void setHeld(Long held) {
        this.held = held;
    }

    public Long getCirculated() {
        return circulated;
    }

    public void setCirculated(Long circulated) {
        this.circulated = circulated;
    }

    public Long getRecycled() {
        return recycled;
    }

    public void setRecycled(Long recycled) {
        this.recycled = recycled;
    }

    @Override
    public String toString() {
        return "IntegralStatInfo{" +
                "issued=" + issued +
                ", held=" + held +
                ", circulated=" + circulated +
                ", recycled=" + recycled +
                '}';
    }
}
