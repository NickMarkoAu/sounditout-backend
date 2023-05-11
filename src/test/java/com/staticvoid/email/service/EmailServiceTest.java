package com.staticvoid.email.service;

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
        emailService.sendEmail("nick.marko.au@gmail.com", "Test Subject", "Test Body");
    }

    @Test
    void should_send_confirm_email() {
        emailService.sendConfirmEmail("nick.marko.au@gmail.com", "https://google.com");
    }

}