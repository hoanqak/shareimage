package com.service.notification;

public class ResponseResult
{
    private String message;
    private Object data;

    private ResponseResult(String message, Object data){
        this.message = message;
        this.data = data;
    }
    public static Object isSuccess(String message, Object data){
        return new ResponseResult(message, data);
    }

    public static Object failed(String message){
        return new ResponseResult(message, null);
    }
}
