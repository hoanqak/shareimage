package com.service;

import com.dto.UserDTO;
import com.entity.User;
import com.service.notification.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface UserService
{
    ResponseResult loginUser(UserDTO userDTO);

    ResponseResult register(UserDTO userDTO, String lang);

    User getUserById(int id);

    void update(User user);

    ResponseResult activeAccount(String code);

    void upload();
}
