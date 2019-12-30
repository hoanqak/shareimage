package com.service;

import com.configuration.HibernateUtils;
import com.entity.AccessToken;
import com.service.base.BaseService;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessTokenServiceImpl extends BaseService implements AccessTokenService
{
    @Autowired HibernateUtils hibernateUtils;
    private String query = "FROM " + AccessToken.class.getName() + " as access WHERE access.accessToken = :accessToken";

    @Override
    public AccessToken getByAccessToken(String accessToken)
    {
        Session session = hibernateUtils.getSessionFactory().openSession();
        try{
            Query query = session.createQuery(this.query);
            query.setParameter("accessToken", accessToken);
            List<AccessToken> result = query.getResultList();
            if(result != null && result.size() > 0){
                return result.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally
        {
            session.close();
        }
        return null;
    }

    @Override
    public AccessToken getByID(int id) {
        return (AccessToken)getById(AccessToken.class.getName(), id);
    }
}
