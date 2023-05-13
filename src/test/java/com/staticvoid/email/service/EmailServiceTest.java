package com.staticvoid.email.service;

import com.staticvoid.user.domain.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    void should_send_email() {
        ApplicationUser user = ApplicationUser.builder()
                .name("Test User")
                .email("test@sounditout.app")
                .build();
        emailService.sendEmail(user, "Test Subject", "Test Body");
    }

    @Test
    void should_send_confirm_email() {
        ApplicationUser user = ApplicationUser.builder()
                .name("Test User")
                .email("test@sounditout.app")
                .build();
        emailService.sendConfirmEmail(user);
    }

    @Test
    void should_send_forgot_password_email() {
        ApplicationUser user = ApplicationUser.builder()
                .name("Test User")
                .email("test@sounditout.app")
                .build();
        emailService.sendForgotEmail(user);
    }

}