package com.controller;

import com.configuration.HibernateUtils;
import com.entity.AccessToken;
import com.entity.User;
import com.service.base.BaseService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class HomeController
{
    @Autowired
    HibernateUtils hibernateUtils;

    @Autowired
    UserService userService;

    @Autowired BaseService baseService;

    @GetMapping("/home")
    public String home(){
        return "verified";
    }

    @GetMapping("/")
    public String index(){
        return "view/login";
    }

    @ResponseBody
    @GetMapping("getUser")
    public void get(){
        System.out.println(baseService.getById(AccessToken.class.getName(), 1));
    }

    @GetMapping("/check-session")
    public boolean checkSession(){
        return hibernateUtils.getSession().isConnected();
    }

    @GetMapping("activated")
    public String activateAccount(@RequestParam("code") String code){
        userService.activeAccount(code);
        return "verified";
    }

    @PostMapping("/login")
    public String login(){
        return "index";
    }
}
