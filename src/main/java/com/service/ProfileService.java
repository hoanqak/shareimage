package com.service;

import com.dto.ProfileDTO;
import com.service.notification.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {
    ResponseResult updateProfile(String accessToken, ProfileDTO profileDTO);
}
