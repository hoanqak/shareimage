package com;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application
{
    @Value("${data.url}")
    String url;
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
