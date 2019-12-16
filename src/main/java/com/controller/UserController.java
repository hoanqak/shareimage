package com.controller;

import com.dto.UserDTO;
import com.service.UserService;
import com.service.notification.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController
{
    @Autowired UserService userService;
    @PostMapping("/singup")
    public ResponseEntity signUp(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.register(userDTO));
    }

    @GetMapping("activated")
    public ResponseResult activateAccount(@RequestParam("code") String code){
        System.out.println(code);
        return null;
    }

    @GetMapping("/")
    public void Hello(){
        System.out.println("Hello");
    }

}
