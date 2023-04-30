package com.staticvoid.invitecode.api;

import com.staticvoid.invitecode.service.InviteCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InviteCodeController {

    private final InviteCodeService inviteCodeService;

    @GetMapping("/api/invite/validate/{code}")
    public ResponseEntity<?> validateCode(@PathVariable("code") String code) {
        try {
            boolean isValid = inviteCodeService.validateCode(code);
            return ResponseEntity.ok(isValid);
        } catch(Exception e) {
            log.error("Error validating code", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
