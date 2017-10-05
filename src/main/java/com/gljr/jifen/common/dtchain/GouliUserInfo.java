package com.gljr.jifen.common.dtchain;

import java.io.Serializable;

public class GouliUserInfo implements Serializable{

    private Long id;

    private String userName;

    private String realName;

    private String phone;

    private Long validValue;

    private Long totalValue;

    public GouliUserInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getValidValue() {
        return validValue;
    }

    public void setValidValue(Long validValue) {
        this.validValue = validValue;
    }

    public Long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Long totalValue) {
        this.totalValue = totalValue;
    }


}
