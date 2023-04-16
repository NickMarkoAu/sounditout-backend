package com.staticvoid.user.api;

import com.staticvoid.songsuggestion.domain.dto.GenerateResultDto;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.user.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApplicationUserController {

    private final ApplicationUserService userDetailsService;

    @GetMapping("/api/user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(ApplicationUserDto.toDto(userDetailsService.loadUserById(userId)));
        } catch (Exception e) {
            log.error("Could not return song suggestions: ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
