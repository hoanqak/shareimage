package com.service.notification;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Locale;
import java.util.Properties;

@Service
public class LocalizationMessage
{
    static Properties properties;

    private final String EN = "message_en.properties";
    private final String VI = "message_vi.properties";

    public String getMessageByLocale(String key, Locale locale) {
        try {
            properties = new Properties();

            File messageFile = new File(LocalizationMessage.class.getResource("/" + EN).toURI());
            if(locale != null){
                if(locale.getLanguage().equals("vi")){
                    messageFile = new File(LocalizationMessage.class.getResource("/" + VI).toURI());
                }
            }
            InputStream inputStream = new FileInputStream(messageFile);
            properties.load(inputStream);
            inputStream.close();
            return properties.getProperty(key);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
