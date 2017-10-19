package com.gljr.jifen.common.dtchain.vo;

import java.io.Serializable;

public class CommonOrderResponse implements Serializable{

    private String blockId;

    private String extOrderId;

    private String refData;

    public CommonOrderResponse() {

    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getExtOrderId() {
        return extOrderId;
    }

    public void setExtOrderId(String extOrderId) {
        this.extOrderId = extOrderId;
    }

    public String getRefData() {
        return refData;
    }

    public void setRefData(String refData) {
        this.refData = refData;
    }

    @Override
    public String toString() {
        return "CommonOrderResponse{" +
                "blockId='" + blockId + '\'' +
                ", extOrderId='" + extOrderId + '\'' +
                ", refData='" + refData + '\'' +
                '}';
    }
}
