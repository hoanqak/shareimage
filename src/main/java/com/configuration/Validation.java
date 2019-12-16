package com.configuration;

public class Validation
{
    static final String REGEX_MAIL = "^[a-z]?[a-zA-Z0-9_\\-\\\\+]{5,20}(@){1}[a-z]{1,7}.\\w{2,4}$";
    static final String REGEX_PHONE = "^[+]{1}?[0-9]{8,16}";
    public static boolean validEmail(String email){
        if(email != null){
            return email.matches(REGEX_MAIL);
        }
        return false;
    }

    public static boolean validPhone(String phone){
        if(phone != null){
            return phone.matches(REGEX_PHONE);
        }
        return false;
    }
}
