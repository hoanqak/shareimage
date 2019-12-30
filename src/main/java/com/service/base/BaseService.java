package com.service.base;

import com.configuration.HibernateUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class BaseService
{

    @Autowired HibernateUtils hibernateUtils;
    public Object getById(String entityName, int id){
        Session session = hibernateUtils.getSessionFactory().openSession();
        Object object =  session.get(entityName, id);
        session.close();
        return object;
    }

    public void delete(Object entity){
        Session session = hibernateUtils.getSessionFactory().openSession();
        try
        {
            session.delete(entity);
            session.beginTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally
        {
            session.close();
        }
    }

    public void update(Object entity){
        Session session = hibernateUtils.getSessionFactory().openSession();
        try {
            session.update(entity);
            session.beginTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    public Serializable save(Object entity){
        Session session = hibernateUtils.getSessionFactory().openSession();
        Serializable serializable = null;
        try {
            serializable = session.save(entity);
            session.beginTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return serializable;
    }
}
