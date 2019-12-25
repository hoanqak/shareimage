package com.service.mail;

import com.service.mail.template.MailService;
import com.service.mail.template.TemplateMail;
import org.springframework.stereotype.Service;

@Service
@TemplateMail("verify.html")
public class SendMailActivateAccount extends MailService
{
    @Override
    public MailService createMail(String sendTo, String subject, String[] parameter)
    {
        this.sendTo = sendTo;
        this.subject =subject;
        this.parameter = parameter;
        return this;
    }
}
