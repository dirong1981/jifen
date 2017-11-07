package com.gljr.jifen.common;

import com.gljr.jifen.constants.GlobalConstants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsonResult implements Serializable{

    private static final long serialVersionUID = 6071196029362896442L;

    private String errorCode;

    private String message;

    private Map<Object, Object> item = new HashMap<Object, Object>();

    public JsonResult(){
        this.errorCode = GlobalConstants.OPERATION_SUCCEED;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<Object, Object> getItem() {
        return item;
    }

    public void setItem(Map<Object, Object> item) {
        this.item = item;
    }

    public void setErrorCodeAndMessage(String[] errorCodeAndMessage){
        this.errorCode = errorCodeAndMessage[0];
        this.message = errorCodeAndMessage[1];
    }
}
