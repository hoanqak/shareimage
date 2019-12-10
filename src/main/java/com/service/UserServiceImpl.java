package com.service;

import com.configuration.HibernateUtils;
import com.dto.UserDTO;
import com.entity.AccessToken;
import com.entity.User;
import com.service.notification.ResponseResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired HibernateUtils hibernateUtils;
    @Autowired DozerService dozerService;

    @Override
    public String loginUser(UserDTO userDTO)
    {
        Session session = hibernateUtils.getSessionFactory().openSession();
        String sql = "FROM " + User.class.getName() + " as u where u.username = :username";
        Query query = session.createQuery(sql);
        query.setParameter("username", userDTO.getUsername());

        List<User> userList = query.getResultList();
        if(userList != null && userList.size() > 0){
            User user =  userList.get(0);

            AccessToken accessToken = getAccessToken(user.getId());
            if(accessToken == null){
                accessToken = new AccessToken();
                accessToken.setAccessToken(RandomStringUtils.randomAlphanumeric(64) + user.getId());
                accessToken.setUser(user);
                session.save(accessToken);
                session.beginTransaction().commit();
            }
            session.close();
            return accessToken.getAccessToken();
        }
        session.close();
        return null;
    }

    @Override
    public ResponseResult register(UserDTO userDTO)
    {
        if(userDTO == null){

        }
        return null;
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
}
