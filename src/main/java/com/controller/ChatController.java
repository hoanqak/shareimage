package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
@ResponseBody
@RequestMapping("/chat")
public class ChatController
{

    @GetMapping("/")
    public String chat(HttpServletResponse response){
        response.setContentType("text/plan");

        return "";
    }

}
