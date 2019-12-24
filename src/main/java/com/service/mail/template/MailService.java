package com.service.mail.template;

import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public abstract class MailService
{
    @Value("${mail.user}")
    private String email;
    @Value("${mail.password}")
    private String password;
    private final String TEMPLATE_MAIL = "/mail/";

    protected String[] parameter;
    protected String sendTo;
    protected String subject;

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

    private String getTemplate() throws Exception
    {
        TemplateMail mail = this.getClass().getAnnotation(TemplateMail.class);
        if(mail == null){
            throw  new Exception("TEMPLATE NOT EXISTS");
        }
        return mail.value();
    }

    private String readTemplate(){
        StringBuilder template = new StringBuilder();
        try
        {
            File file = new File(MailService.class.getResource(TEMPLATE_MAIL + getTemplate()).toURI());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = reader.readLine();
            while (line != null){
                template.append(line);
                line = reader.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return template.toString();
    }

    private MimeBodyPart createMimeBodyPart() throws MessagingException
    {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setHeader("Content-Type", "text/html; charset=\"utf-8\"");
        String content = String.format(readTemplate(), this.parameter);
        mimeBodyPart.setContent(content, "text/html; charset=UTF-8");
        return mimeBodyPart;
    }

    private Message getMessage() throws MessagingException
    {
        Message message = new MimeMessage(getSession());
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.sendTo));
        message.setSubject(this.subject);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(createMimeBodyPart());
        message.setContent(multipart);
        return message;
    }

    public void sendService(){
        System.out.println("Sending to " + this.sendTo +"..................");
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Transport.send(getMessage());
                    System.out.println("Send done to " + sendTo);
                }
                catch (MessagingException e)
                {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public abstract MailService createMail(String sendTo, String subject, String parameter[]);


}
