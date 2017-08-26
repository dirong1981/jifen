package com.gljr.jifen.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class Category {
    private String cId;

    private String cName;

    private String cParentId;

    private String cLogo;

    private Integer cState;

    private String cSort;

    private String cCreator;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId == null ? null : cId.trim();
    }

    @NotBlank
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName == null ? null : cName.trim();
    }

    @NotBlank
    public String getcParentId() {
        return cParentId;
    }

    public void setcParentId(String cParentId) {
        this.cParentId = cParentId == null ? null : cParentId.trim();
    }

    public String getcLogo() {
        return cLogo;
    }

    public void setcLogo(String cLogo) {
        this.cLogo = cLogo == null ? null : cLogo.trim();
    }

    @NotNull
    public Integer getcState() {
        return cState;
    }

    public void setcState(Integer cState) {
        this.cState = cState;
    }

    @NotBlank
    public String getcSort() {
        return cSort;
    }

    public void setcSort(String cSort) {
        this.cSort = cSort == null ? null : cSort.trim();
    }

    @NotBlank
    public String getcCreator() {
        return cCreator;
    }

    public void setcCreator(String cCreator) {
        this.cCreator = cCreator == null ? null : cCreator.trim();
    }
}