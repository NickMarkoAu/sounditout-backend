package com.staticvoid.profile.api;

import com.staticvoid.fileupload.service.S3StorageService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.repository.ImageRepository;
import com.staticvoid.profile.domain.dto.UserProfileDto;
import com.staticvoid.profile.service.UserProfileService;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final S3StorageService storageService = new S3StorageService();
    private final ImageRepository imageRepository;

    @PostMapping("/api/profile/user")
    public ResponseEntity<?> getUserProfile(@RequestBody ApplicationUserDto user, Pageable pageable) {
        try {
            return ResponseEntity.ok(userProfileService.getUserProfile(user, pageable));
        } catch (Exception e) {
            log.error("Could not return user userProfile: ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/api/profile/user/update")
    public ResponseEntity<?> getUserProfile(@RequestBody UserProfileDto userProfile) {
        try {
            return ResponseEntity.ok(userProfileService.updateUserProfile(userProfile));
        } catch (Exception e) {
            log.error("Could not update user userProfile: ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/api/profile/user/update/picture")
    public ResponseEntity<?> updateProfilePicture(@RequestParam("file") MultipartFile file) {
        try {
            ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Image convFile = storageService.store(file, user);
            imageRepository.save(convFile);
            return ResponseEntity.ok(userProfileService.updateProfilePicture(convFile));
        } catch (Exception e) {
            log.error("Could not update user profile picture: ", e);
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
