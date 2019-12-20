package com.service;

import com.configuration.HibernateUtils;
import com.configuration.Validation;
import com.dto.UserDTO;
import com.entity.AccessToken;
import com.entity.Image;
import com.entity.User;
import com.service.base.BaseService;
import com.service.mail.SendMailSMTP;
import com.service.mail.SendMailService;
import com.service.mail.TemplateMail;
import com.service.notification.ErrorCode;
import com.service.notification.LocalizationMessage;
import com.service.notification.ResponseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.*;
import java.util.*;

@Service
public class UserServiceImpl extends BaseService implements UserService
{
    @Autowired HibernateUtils hibernateUtils;
    @Autowired DozerService dozerService;
    @Autowired MD5 md5;
    @Autowired SendMailService sendMailService;
    @Autowired SendMailSMTP sendMail;
    @Autowired LocalizationMessage localizationMessage;
    @Value("${cus.host}")
    private String host;
    private String queryFindByUsername = "FROM " +User.class.getName() +" as u where u.username = :username";
    private String apiVerify = "api/activated?code=";

    @Override
    public ResponseResult loginUser(UserDTO userDTO)
    {
        Session session = hibernateUtils.getSessionFactory().openSession();
        Query query = session.createQuery(queryFindByUsername);
        query.setParameter("username", userDTO.getUsername().trim());

        List<User> userList = query.getResultList();
        if(userList != null && userList.size() > 0){
            User user =  userList.get(0);
            if(!md5.convertToMD5(userDTO.getPassword()).equals(user.getPassword())){
                return ResponseResult.failed(ErrorCode.WRONG_USERNAME_OR_PASSWORD);
            }
            AccessToken accessToken = getAccessToken(user.getId());
            if(accessToken == null){
                accessToken = new AccessToken();
                accessToken.setAccessToken(RandomStringUtils.randomAlphanumeric(64) + user.getId());
                accessToken.setUser(user);
                save(accessToken);
            }
            session.close();
            Map map =new HashMap();
            map.put("accessToken", accessToken.getAccessToken());
            return ResponseResult.isSuccess(ErrorCode.SUCCESS, map);
        }
        session.close();
        return ResponseResult.failed(ErrorCode.WRONG_USERNAME_OR_PASSWORD);
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
            return ResponseResult.failed(ErrorCode.USER_EXISTS);
        }

        if(userDTO.getEmail() == null || !Validation.validEmail(userDTO.getEmail())){
            return ResponseResult.failed(ErrorCode.IVALID_EMAIL);
        }

        if(userDTO.getPassword() != null && userDTO.getRePassword() != null){
            if(userDTO.getPassword().equals(userDTO.getRePassword())){
                User user = new User();
                user.setUsername(userDTO.getUsername());
                user.setPassword(md5.convertToMD5(userDTO.getPassword()));
                user.setEmail(userDTO.getEmail());
                user.setActive(false);
                int id = (int) save(user);
                String templateMailActive = TemplateMail.getTemplateVerifyAccount();
                String linkActive = String.format(templateMailActive, host + apiVerify + md5.convertToMD5(String.valueOf(userDTO.getUsername())) +"-"+id);
                String subject = localizationMessage.getMessageByLocale("label.verify.account", locale);
                sendMail.send(userDTO.getEmail(), subject, linkActive);
                return ResponseResult.isSuccess(ErrorCode.SUCCESS, null);
            }
        }
        session.close();
        return ResponseResult.failed(ErrorCode.PASSWORD_IVALID);
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
            User user = (User) getById(new User(), Integer.parseInt(code.split("-")[1]));
            if(user != null)
            {
                if(user.isActive()){
                    return ResponseResult.isSuccess(ErrorCode.SUCCESS, null);
                }
                user.setActive(true);
                update(user);
                return ResponseResult.isSuccess(ErrorCode.SUCCESS, null);
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            return ResponseResult.failed(ErrorCode.ERROR_CODE_VERIFY);
        }
        return ResponseResult.failed(ErrorCode.USER_NOT_EXISTS);
    }

    public void upload(){
        File file = new File(File.separator + "home.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Image image = new Image();
            image.setSize((int) file.length());
            image.setFileName(file.getName());
            image.setImage(bFile);
            save(image);
    }
}
