package com.configuration;

import com.entity.AccessToken;
import com.entity.Image;
import com.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Scope(scopeName = "prototype")
public class HibernateUtils
{
    @Value("${data.username}")
    private String USERNAME;
    @Value("${data.password}")
    private String PASSWORD;
    @Value("${data.url}")
    private String URL;
    @Value("${data.driver}")
    private String DRIVER;

    public Session getSession(){
        Properties properties = getProperties();
        Configuration configuration = getConfiguration(properties);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory.openSession();
    }

    public SessionFactory getSessionFactory(){
        Configuration configuration = getConfiguration(getProperties());
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static Configuration getConfiguration(Properties properties){
        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(AccessToken.class);
        configuration.addAnnotatedClass(Image.class);
        return configuration;
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        properties.put(Environment.URL, URL);
        properties.put(Environment.DRIVER, DRIVER);
        properties.put(Environment.USER, USERNAME);
        properties.put(Environment.PASS, PASSWORD);
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        return properties;
    }

}
