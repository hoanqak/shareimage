package com.service.notification;

import lombok.Data;

@Data
public class ResponseResult
{
    private String message;
    private Object data;

    public ResponseResult(String message, Object data){
        this.message = message;
        this.data = data;
    }
    public static ResponseResult isSuccess(String message, Object data){
        return new ResponseResult(message, data);
    }

    public static ResponseResult failed(String message){
        return new ResponseResult(message, null);
    }
}
