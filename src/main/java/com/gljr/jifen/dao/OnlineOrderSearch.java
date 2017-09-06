package com.gljr.jifen.dao;

public class OnlineOrderSearch {
    private Byte status;
    private String Logmin;
    private String Logmax;


    public String getLogmin() {
        return Logmin.trim();
    }

    public String getLogmax() {
        return Logmax.trim();
    }

    public void setLogmin(String logmin) {
        Logmin = logmin;
    }

    public void setLogmax(String logmax) {
        Logmax = logmax;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}
