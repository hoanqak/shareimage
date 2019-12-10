package com.controller;

import com.configuration.HibernateUtils;
import com.dto.UserDTO;
import com.entity.User;
import com.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HomeController
{
    @Autowired
    HibernateUtils hibernateUtils;

    @Autowired
    UserService userService;

    @GetMapping("/home")
    public void home(){
        Session session = hibernateUtils.getSession();
        User user = new User();

        user.setUsername("admin");
        user.setPassword("123123");
        session.getTransaction();
        session.save(user);
        session.beginTransaction().commit();
        session.close();
    }

    @GetMapping("/check-session")
    public boolean checkSession(){
        return hibernateUtils.getSession().isConnected();
    }

    @PostMapping
    public String login(@RequestBody UserDTO user){
        System.out.println(RandomStringUtils.randomAlphanumeric(64));
        return userService.loginUser(user);
    }
}
