package com.staticvoid.profile.api;

import com.staticvoid.profile.service.UserProfileService;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/api/profile/user")
    public ResponseEntity<?> getUserProfile(@RequestBody ApplicationUserDto user, Pageable pageable) {
        try {
            return ResponseEntity.ok(userProfileService.getUserProfile(user, pageable));
        } catch (Exception e) {
            log.error("Could not return user userProfile: ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/api/profile/user/follow")
    public ResponseEntity<?> followUser(@RequestBody ApplicationUserDto user) {
        try {
            return ResponseEntity.ok(userProfileService.followUser(user));
        } catch (Exception e) {
            log.error("Could not follow user: ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}