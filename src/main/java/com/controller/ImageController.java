package com.controller;

import com.service.AccessTokenService;
import com.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ImageController
{
    @Autowired ImageService imageService;
    @Autowired AccessTokenService accessTokenService;
    @GetMapping("/image")
    public void image(@RequestParam("fileName")String fileName, HttpServletResponse response){
        imageService.getEmailByName(fileName, response);
    }
}
