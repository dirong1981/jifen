package com.gljr.jifen.pojo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class Admin {
    private String aId;

    private String aName;

    private String aPassword;

    private Date aLasttime;

    private String aSalt;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId == null ? null : aId.trim();
    }

    @NotBlank
    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName == null ? null : aName.trim();
    }

    @NotBlank
    public String getaPassword() {
        return aPassword;
    }

    public void setaPassword(String aPassword) {
        this.aPassword = aPassword == null ? null : aPassword.trim();
    }

    public Date getaLasttime() {
        return aLasttime;
    }

    public void setaLasttime(Date aLasttime) {
        this.aLasttime = aLasttime;
    }

    public String getaSalt() {
        return aSalt;
    }

    public void setaSalt(String aSalt) {
        this.aSalt = aSalt == null ? null : aSalt.trim();
    }
}