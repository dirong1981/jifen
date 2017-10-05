package com.gljr.jifen.exception;

/**
 * Created by Administrator on 2017/9/27 0027.
 */
public class ApiServerException extends RuntimeException{
    public ApiServerException() {
        super();
    }

    public ApiServerException(String message) {
        super(message);
    }

    public ApiServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiServerException(Throwable cause) {
        super(cause);
    }

    protected ApiServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
