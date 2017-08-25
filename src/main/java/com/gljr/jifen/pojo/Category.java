package com.gljr.jifen.pojo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class Category {
    private Integer cId;

    private String cName;

    private Integer cParentId;

    private String cLogo;

    private Integer cState;

    private String cSort;

    private Integer cCreator;

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    @NotBlank
    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName == null ? null : cName.trim();
    }

    @NotNull
    public Integer getcParentId() {
        return cParentId;
    }

    public void setcParentId(Integer cParentId) {
        this.cParentId = cParentId;
    }

    @NotBlank
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

    @NotNull
    public Integer getcCreator() {
        return cCreator;
    }

    public void setcCreator(Integer cCreator) {
        this.cCreator = cCreator;
    }
}