package com.gljr.jifen.pojo;

public class UserExtInfo {
    private Integer id;

    private String cellphone;

    private Byte viewType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone == null ? null : cellphone.trim();
    }

    public Byte getViewType() {
        return viewType;
    }

    public void setViewType(Byte viewType) {
        this.viewType = viewType;
    }
}