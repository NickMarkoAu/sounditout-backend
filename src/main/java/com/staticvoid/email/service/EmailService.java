package com.staticvoid.email.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.staticvoid.user.domain.ConfirmationToken;
import com.staticvoid.user.domain.PasswordResetToken;
import com.staticvoid.user.respository.ConfirmationTokenRepository;
import com.staticvoid.user.respository.PasswordResetTokenRespository;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EmailService {

    private final String apiKey;
    private final String fromEmail;
    private final String fromName;

    private final String confirmEmailTemplate;
    private final String confirmEmailBaseUrl;

    private final String forgotPasswordTemplate;
    private final String forgotPasswordBaseUrl;

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordResetTokenRespository passwordResetTokenRespository;

    public EmailService(@Value("${spring.sendgrid.api-key}") String apiKey,
                        @Value("${spring.sendgrid.fromEmail}")String fromEmail,
                        @Value("${spring.sendgrid.fromName}") String fromName,
                        @Value("${spring.sendgrid.confirmEmailTemplate}") String confirmEmailTemplate,
                        @Value("${spring.sendgrid.confirmEmailBaseUrl}") String confirmEmailBaseUrl,
                        @Value("${spring.sendgrid.forgotPasswordTemplate}") String forgotPasswordTemplate,
                        @Value("${spring.sendgrid.forgotPasswordBaseUrl}") String forgotPasswordBaseUrl,
                        @Autowired ConfirmationTokenRepository confirmationTokenRepository,
                        @Autowired PasswordResetTokenRespository passwordResetTokenRespository) {
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
        this.confirmEmailTemplate = confirmEmailTemplate;
        this.confirmEmailBaseUrl = confirmEmailBaseUrl;
        this.forgotPasswordTemplate = forgotPasswordTemplate;
        this.forgotPasswordBaseUrl = forgotPasswordBaseUrl;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordResetTokenRespository = passwordResetTokenRespository;
    }

    public void sendEmail(ApplicationUser user, String subject, String body) {
        log.info("Sending email to: " + user.getEmail() + " with subject: " + subject + " and body: " + body);
        Email fromEmail = new Email(this.fromEmail);
        fromEmail.setName(this.fromName);
        Email toEmail = new Email(user.getEmail());
        toEmail.setName(user.getName());
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        sendMail(mail);
    }

    public void sendConfirmEmail(ApplicationUser user) {
        Email fromEmail = new Email(this.fromEmail);
        fromEmail.setName(this.fromName);
        Email toEmail = new Email();
        toEmail.setEmail(user.getEmail());
        toEmail.setName(user.getName());
        Mail mail = new Mail();
        mail.setTemplateId(confirmEmailTemplate);
        mail.setFrom(fromEmail);
        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);
        personalization.addDynamicTemplateData("url", buildConfirmUrl(user));
        mail.addPersonalization(personalization);
        sendMail(mail);
    }

    public void sendForgotEmail(ApplicationUser user) {
        Email fromEmail = new Email(this.fromEmail);
        fromEmail.setName(this.fromName);
        Email toEmail = new Email();
        toEmail.setEmail(user.getEmail());
        toEmail.setName(user.getName());
        Mail mail = new Mail();
        mail.setTemplateId(forgotPasswordTemplate);
        mail.setFrom(fromEmail);
        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);
        personalization.addDynamicTemplateData("url", buildForgotUrl(user));
        personalization.addDynamicTemplateData("name", user.getName());
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

    private String buildConfirmUrl(ApplicationUser user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);
        return confirmEmailBaseUrl + "?token=" + confirmationToken.getConfirmationToken();
    }

    private String buildForgotUrl(ApplicationUser user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user);
        passwordResetTokenRespository.save(passwordResetToken);
        return forgotPasswordBaseUrl + "?token=" + passwordResetToken.getPasswordResetToken();
    }
}
