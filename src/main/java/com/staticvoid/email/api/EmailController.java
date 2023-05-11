package com.staticvoid.email.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    @GetMapping("/api/email/confirm")
    //TODO use spring security registration here to confirm email after registration
    public ResponseEntity<?> confirmEmail() {
        try {
            return ResponseEntity.ok("Email confirmed");
        } catch (Exception e) {
            log.error("Could not confirm email: ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
