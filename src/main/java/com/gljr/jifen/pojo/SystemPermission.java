package com.gljr.jifen.pojo;

public class SystemPermission {
    private Integer id;

    private Integer parentCode;

    private Integer code;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentCode() {
        return parentCode;
    }

    public void setParentCode(Integer parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}