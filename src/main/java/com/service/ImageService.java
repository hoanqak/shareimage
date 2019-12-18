package com.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface ImageService
{
    void getEmailByName(String fileName, HttpServletResponse response);
}
