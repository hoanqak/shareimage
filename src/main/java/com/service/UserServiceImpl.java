package com.service;

import com.configuration.HibernateUtils;
import com.configuration.Validation;
import com.dto.UserDTO;
import com.entity.AccessToken;
import com.entity.User;
import com.service.mail.SendMailSMTP;
import com.service.mail.SendMailService;
import com.service.notification.ErrorCode;
import com.service.notification.ResponseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired HibernateUtils hibernateUtils;
    @Autowired DozerService dozerService;
    @Autowired MD5 md5;
    private String queryFindByUsername = "FROM " +User.class.getName() +" as u where u.username = :username";
    @Autowired SendMailService sendMailService;
    @Autowired SendMailSMTP sendMail;
    @Override
    public ResponseResult loginUser(UserDTO userDTO)
    {
        Session session = hibernateUtils.getSessionFactory().openSession();
        String sql = "FROM " + User.class.getName() + " as u where u.username = :username";
        Query query = session.createQuery(sql);
        query.setParameter("username", userDTO.getUsername());

        List<User> userList = query.getResultList();
        if(userList != null && userList.size() > 0){
            User user =  userList.get(0);
            if(md5.convertToMD5(userDTO.getPassword()).equals(user.getPassword())){
                return ResponseResult.failed(ErrorCode.WRONG_USERNAME_OR_PASSWORD);
            }
            AccessToken accessToken = getAccessToken(user.getId());
            if(accessToken == null){
                accessToken = new AccessToken();
                accessToken.setAccessToken(RandomStringUtils.randomAlphanumeric(64) + user.getId());
                accessToken.setUser(user);
                session.save(accessToken);
                session.beginTransaction().commit();
            }
            session.close();
            return ResponseResult.isSuccess(ErrorCode.SUCCESS, new HashMap<>().put("accessToken", accessToken.getAccessToken()));
        }
        session.close();
        return ResponseResult.failed(ErrorCode.WRONG_USERNAME_OR_PASSWORD);
    }

    @Override
    public ResponseResult register(UserDTO userDTO)
    {
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
                int id = (int) session.save(user);
                session.beginTransaction().commit();
                session.close();
                String linkActive = String.format("<a href='%s'>GO</a>", "http://localhost:8081/api/activated?code=" + md5.convertToMD5(String.valueOf(userDTO.getUsername())) +"-"+id);
                sendMail.send(userDTO.getEmail(), "Verify account", linkActive);
                return ResponseResult.isSuccess(ErrorCode.SUCCESS, null);
            }
        }
        session.close();
        return ResponseResult.failed(ErrorCode.PASSWORD_IVALID);
    }

    @Override
    public User getUserById(int id)
    {
        Session session = hibernateUtils.getSessionFactory().openSession();
        User user =  session.get(User.class, id);
        session.close();
        return user;
    }

    public void update(User user){
        Session session = hibernateUtils.getSessionFactory().openSession();
        session.update(user);
        session.beginTransaction().commit();
        session.close();
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
        User user = getUserById(Integer.parseInt(code.split("-")[1]));
        if(user != null)
        {
            if(user.isActive()){
                return ResponseResult.isSuccess(ErrorCode.SUCCESS, null);
            }
            user.setActive(true);
            update(user);
            return ResponseResult.isSuccess(ErrorCode.SUCCESS, null);
        }
        return ResponseResult.failed(ErrorCode.USER_NOT_EXISTS);
    }
}
