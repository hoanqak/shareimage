package com.controller;

import com.dto.UserDTO;
import com.entity.AccessToken;
import com.service.AccessTokenService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserController
{
    @Autowired UserService userService;
    @Autowired AccessTokenService accessTokenService;


    @PostMapping("/singup")
    public ResponseEntity signUp(@RequestBody UserDTO userDTO, @RequestParam("lang") String lang){
        return ResponseEntity.ok(userService.register(userDTO, lang));
    }
    @PostMapping("/loginApi")
    public ResponseEntity login(@RequestBody UserDTO user){
        return ResponseEntity.ok(userService.loginUser(user));
    }

    @GetMapping("activeAPI")
    public ResponseEntity activateAccount(@RequestParam("code") String code){
        return ResponseEntity.ok(userService.activeAccount(code));
    }

    @PostMapping("/profile")
    public ResponseEntity updateProfile(HttpServletRequest request){
        return null;
    }

    @GetMapping("/testHeader")
    public String getHeader(HttpServletRequest request){
        String accessToken = request.getHeader("accessToken");
        AccessToken accessToken1 = accessTokenService.getByAccessToken(accessToken);
        System.out.println(accessToken1.toString());
        return request.getHeader("AccessToken");
    }


}
