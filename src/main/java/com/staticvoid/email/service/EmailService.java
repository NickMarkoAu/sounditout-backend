package com.staticvoid.email.service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EmailService {

    private final String apiKey;
    private final String from;

    public EmailService(@Value("${spring.sendgrid.api-key}") String apiKey, @Value("${spring.sendgrid.from}")String from) {
        this.apiKey = apiKey;
        this.from = from;
    }

    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to: " + to + " with subject: " + subject + " and body: " + body);
        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        sendMail(mail);
    }

    public void sendConfirmEmail(String to, String url) {
        Email fromEmail = new Email(from);
        Email toEmail = new Email();
        toEmail.setEmail(to);
        Mail mail = new Mail();
        mail.setTemplateId("d-fbb4a70adf284346b45e7b833b73816f");
        mail.setFrom(fromEmail);
        mail.addCustomArg("{url}", url);
        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);
        mail.addPersonalization(personalization);
        sendMail(mail);
    }

    private void sendMail(Mail mail) {
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("Send email response: {} ", response.getBody());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
