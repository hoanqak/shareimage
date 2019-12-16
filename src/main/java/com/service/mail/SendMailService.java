package com.service.mail;

import com.sendgrid.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SendMailService
{
    Response sendMail(String subject, String content, List<String> sendTo);
}
