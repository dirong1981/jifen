package com.gljr.jifen.common.dtchain.vo;

import java.io.Serializable;

public class GouliUserId implements Serializable {

    private Long id;

    public GouliUserId() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GouliUserId{" +
                "id=" + id +
                '}';
    }
}
