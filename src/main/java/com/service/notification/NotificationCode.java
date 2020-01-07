package com.service.notification;

public class NotificationCode
{
    public interface ErrorCode
    {
        String WRONG_USERNAME_OR_PASSWORD = "WRONG_USERNAME_OR_PASSWORD";
        String USER_EXISTS = "USER_EXISTS";
        String USER_NOT_EXISTS = "USER_NOT_EXISTS";

        String PASSWORD_IVALID = "PASSWORD_IVALID";
        String SUCCESS = "SUCCESS";
        String INVALID_EMAIL = "INVALID_EMAIL";
        String ERROR_CODE_VERIFY = "ERROR_CODE_VERIFY";
        String INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN";
        String WRONG_ACCESS_TOKEN = "WRONG_ACCESS_TOKEN";
    }

    public interface SuccessCode{
        String SUCCESS = "SUCCESS";
    }

}
