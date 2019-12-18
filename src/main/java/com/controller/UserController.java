package com.controller;

import com.dto.UserDTO;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController
{
    @Autowired UserService userService;
    @PostMapping("/singup")
    public ResponseEntity signUp(@RequestBody UserDTO userDTO, @RequestParam("lang") String lang){
        return ResponseEntity.ok(userService.register(userDTO, lang));
    }
    @GetMapping("activated")
    public ResponseEntity activateAccount(@RequestParam("code") String code){
        return ResponseEntity.ok(userService.activeAccount(code));
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDTO user){
        return ResponseEntity.ok(userService.loginUser(user));
    }

    @GetMapping("/upload")
    public void upload(){
        userService.upload();
    }

}
