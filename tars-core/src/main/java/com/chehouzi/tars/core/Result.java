package com.chehouzi.tars.core;


import lombok.Data;

import java.io.Serializable;

/**
 * Created by twg on 2018/2/23.
 * Ajax请求返回值
 */
@Data
public final class Result<T> implements Serializable {

    private static final long serialVersionUID = -4670614307979943941L;
    private static final int OK = 200;
    private int code;
    private boolean success;
    private String message;
    private T data;

    public static <T> Result<T> buildSuccessResult(String message) {
        Result<T> result = new Result<T>();
        result.setCode(OK);
        result.setSuccess(true);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> buildSuccessResult(T data, String message) {
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setCode(OK);
        result.setSuccess(true);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> buildSuccessResult(T data) {
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setCode(OK);
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> buildErrorResult(int code, T data) {
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setCode(code);
        result.setSuccess(false);
        return result;
    }

    public static <T> Result<T> buildErrorResult(int code, String message) {
        Result<T> result = new Result<T>();
        result.setMessage(message);
        result.setCode(code);
        result.setSuccess(false);
        return result;
    }
}
