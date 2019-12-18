package com.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
public class SendMailSMTP
{
    @Value("${mail.user}")
    private String email;
    @Value("${mail.password}")
    private String password;

    private static Properties getProperties(){
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
        prop.put("mail.smtp.socketFactory.port", "587");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return prop;
    }

    private Session getSession(){
        return Session.getDefaultInstance(getProperties(), new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    private Message getMessage(String to, String subject, String content) throws MessagingException
    {
        Message message = new MimeMessage(getSession());
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setHeader("Content-Type", "text/html; charset=\"utf-8\"");
        mimeBodyPart.setContent(content, "text/html; charset=UTF-8");


        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        return message;
    }

    public void send(String to, String subject, String content){
            System.out.println("Sending to " + to +"..................");
            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Transport.send(getMessage(to, subject, content));
                    }
                    catch (MessagingException e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();
    }

}
