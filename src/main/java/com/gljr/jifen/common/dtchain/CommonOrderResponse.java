package com.gljr.jifen.common.dtchain;

import java.io.Serializable;

public class CommonOrderResponse implements Serializable{

    private String blockId;

    private String extOrderId;

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

    @Override
    public String toString() {
        return "CommonOrderResponse{" +
                "blockId='" + blockId + '\'' +
                ", extOrderId='" + extOrderId + '\'' +
                '}';
    }
}
