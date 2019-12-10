package com.controller;

import com.dto.UserDTO;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController
{
    @Autowired UserService userService;
    @PostMapping("/singup")
    public ResponseEntity signUp(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.register(userDTO));
    }
}
