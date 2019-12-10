package com.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Service
public class MD5
{
    public String convertToMD5(String input){
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());
            BigInteger bigInteger = new BigInteger(1, messageDigest.digest());
            return bigInteger.toString(16);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
