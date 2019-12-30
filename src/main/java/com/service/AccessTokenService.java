package com.service;

import com.entity.AccessToken;
import org.springframework.stereotype.Service;

@Service
public interface AccessTokenService
{

    AccessToken getByAccessToken(String accessToken);

    AccessToken getByID(int id);
}
