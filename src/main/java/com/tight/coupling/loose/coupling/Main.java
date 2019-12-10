package com.tight.coupling.loose.coupling;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class Main
{
    public static void main(String[] args)
    {
        String hello = "HelloWorld";
        String encode = Base64.encode(hello.getBytes());

        System.out.println(encode);
    }
}
