package com.gljr.jifen.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

public class Category {
    private Integer id;

    private Byte type;

    private Integer parentCode;

    private Integer code;

    private String name;

    private String logoKey;

    private Byte status;

    private Integer sort;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @NotNull
    public Integer getParentCode() {
        return parentCode;
    }

    public void setParentCode(Integer parentCode) {
        this.parentCode = parentCode;
    }

    @Null
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @Null
    public String getLogoKey() {
        return logoKey;
    }

    public void setLogoKey(String logoKey) {
        this.logoKey = logoKey == null ? null : logoKey.trim();
    }

    @NotNull
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @NotNull
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Null
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}