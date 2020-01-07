package com.service;

import com.configuration.HibernateUtils;
import com.configuration.Validation;
import com.dto.UserDTO;
import com.entity.AccessToken;
import com.entity.User;
import com.service.base.BaseService;
import com.service.mail.SendMailActivateAccount;
import com.service.notification.NotificationCode;
import com.service.notification.LocalizationMessage;
import com.service.notification.ResponseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.*;

@Service
public class UserServiceImpl extends BaseService implements UserService
{
    @Autowired HibernateUtils hibernateUtils;
    @Autowired DozerService dozerService;
    @Autowired SendMailActivateAccount sendMail;
    @Autowired LocalizationMessage localizationMessage;
    @Autowired AccessTokenService accessTokenService;
    @Value("${cus.host}")
    private String host;
    @Value("${server.port}")
    private int port;
    private String queryFindByUsername = "FROM " +User.class.getName() +" as u where u.username = :username";
    private String apiVerify = "/api/activeAPI?code=";
    @Value("${mail.user}")
    private String email;
    @Override
    public ResponseResult loginUser(UserDTO userDTO)
    {
        Session session = hibernateUtils.getSessionFactory().openSession();
        Query query = session.createQuery(queryFindByUsername);
        query.setParameter("username", userDTO.getUsername().trim());

        List<User> userList = query.getResultList();
        if(userList != null && userList.size() > 0){
            User user =  userList.get(0);
            if(!MD5Converter.convertToMD5(userDTO.getPassword()).equals(user.getPassword())){
                return ResponseResult.failed(NotificationCode.ErrorCode.WRONG_USERNAME_OR_PASSWORD);
            }
            AccessToken accessToken = accessTokenService.getByID(user.getId());
            if(accessToken == null){
                accessToken = new AccessToken();
                accessToken.setAccessToken(RandomStringUtils.randomAlphanumeric(64) + user.getId());
                accessToken.setUser(user);
                save(accessToken);
            }
            session.close();
            Map map =new HashMap();
            map.put("accessToken", accessToken.getAccessToken());
            return ResponseResult.isSuccess(NotificationCode.SuccessCode.SUCCESS, map);
        }
        session.close();
        return ResponseResult.failed(NotificationCode.ErrorCode.WRONG_USERNAME_OR_PASSWORD);
    }

    @Override
    public ResponseResult register(UserDTO userDTO, String lang)
    {
        Locale locale = Locale.getDefault();
        if(lang != null){
            locale = new Locale(lang);
        }
        Session session = hibernateUtils.getSessionFactory().openSession();
        Query query = session.createQuery(queryFindByUsername);
        query.setParameter("username", userDTO.getUsername());
        List userList = query.getResultList();
        if(userList != null && userList.size() > 0){
            return ResponseResult.failed(NotificationCode.ErrorCode.USER_EXISTS);
        }

        if(userDTO.getEmail() == null || !Validation.validEmail(userDTO.getEmail())){
            return ResponseResult.failed(NotificationCode.ErrorCode.INVALID_EMAIL);
        }

        if(userDTO.getPassword() != null && userDTO.getRePassword() != null){
            if(userDTO.getPassword().equals(userDTO.getRePassword())){
                User user = new User();
                user.setUsername(userDTO.getUsername());
                user.setPassword(MD5Converter.convertToMD5(userDTO.getPassword()));
                user.setEmail(userDTO.getEmail());
                user.setActive(false);
                int id = (int) save(user);
                String subject = localizationMessage.getMessageByLocale("label.verify.account", locale);
                sendMail.createMail(userDTO.getEmail(), subject, new String[] {
                        host + ":" + port + apiVerify + MD5Converter.convertToMD5(String.valueOf(userDTO.getUsername())) +"-"+id,
                        email}).sendService();
                return ResponseResult.isSuccess(NotificationCode.SuccessCode.SUCCESS, null);
            }
        }
        session.close();
        return ResponseResult.failed(NotificationCode.ErrorCode.PASSWORD_IVALID);
    }

    public AccessToken getAccessToken(int userID){
        Session session = hibernateUtils.getSessionFactory().openSession();
        String sql = "FROM "+AccessToken.class.getName() +" as a where a.user.id = :userId";
        Query query = session.createQuery(sql);
        query.setParameter("userId", userID);
        List<AccessToken> accessTokens = query.getResultList();
        session.close();
        if(accessTokens != null && accessTokens.size() > 0){
            return accessTokens.get(0);
        }
        return null;
    }

    @Override
    public ResponseResult activeAccount(String code){
        try
        {
            User user = (User) getById(User.class.getName(), Integer.parseInt(code.split("-")[1]));
            if(user != null)
            {
                if(user.isActive()){
                    return ResponseResult.isSuccess(NotificationCode.SuccessCode.SUCCESS, null);
                }
                user.setActive(true);
                update(user);
                return ResponseResult.isSuccess(NotificationCode.SuccessCode.SUCCESS, null);
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            return ResponseResult.failed(NotificationCode.ErrorCode.ERROR_CODE_VERIFY);
        }
        return ResponseResult.failed(NotificationCode.ErrorCode.USER_NOT_EXISTS);
    }

    @Override
    public ResponseResult updateProfile(String accessToken)
    {
        if(StringUtils.isEmpty(accessToken)){
            return ResponseResult.failed(NotificationCode.ErrorCode.INVALID_ACCESS_TOKEN);
        }
        return null;
    }
}
