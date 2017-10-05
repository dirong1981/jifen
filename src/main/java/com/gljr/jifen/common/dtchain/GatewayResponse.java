package com.gljr.jifen.common.dtchain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class GatewayResponse<T> {

    private int code;

    private String message;

    private T content;

    public GatewayResponse() {

    }

    public GatewayResponse(int code, String message, T content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
