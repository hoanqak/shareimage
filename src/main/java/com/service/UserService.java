package com.service;

import com.dto.UserDTO;
import com.service.notification.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface UserService
{
    String loginUser(UserDTO userDTO);
    ResponseResult register(UserDTO userDTO);
}
