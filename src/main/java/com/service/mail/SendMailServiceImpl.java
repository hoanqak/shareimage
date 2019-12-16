package com.service.mail;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SendMailServiceImpl implements SendMailService
{

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.name.app}")
    private String nameApp;

    @Value("${sendgrid.api.key}")
    private String apiKey;

    private static final String KEY_X_MOCK = "X-Mock";

    private static final String SEND_GRID_ENDPOINT_SEND_EMAIL = "mail/send";
    @Override
    public Response sendMail(String subject, String content, List<String> sendTo)
    {
        Mail mail = buildMailToSend(subject, content, sendTo);
        SendGrid sendGrid = new SendGrid(apiKey);

        Request request = new Request();
        try
        {
            request.setMethod(Method.POST);
            request.setEndpoint(SEND_GRID_ENDPOINT_SEND_EMAIL);
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            return response;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }



    private Mail buildMailToSend(String subject, String content, List<String> sendTo){
        Mail mail = new Mail();

        Email emailFrom = new Email();
        emailFrom.setEmail(mailFrom);
        emailFrom.setName(nameApp);

        mail.setFrom(emailFrom);
        mail.setSubject(subject);

        Personalization personalization = new Personalization();

        /**
         * Process list mail
         * * */
        for (String email : sendTo)
        {
            Email to = new Email();
            to.setEmail(email);
            personalization.addTo(to);
        }

        mail.addPersonalization(personalization);

        Content contentObj = new Content();
        contentObj.setType("text/plan");
        contentObj.setValue(content);

        mail.addContent(contentObj);

        return mail;
    }
}
