package com.staticvoid.userProfile.service;

import com.staticvoid.post.service.PostService;
import com.staticvoid.userProfile.domain.UserProfile;
import com.staticvoid.userProfile.domain.dto.UserProfileDto;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserProfileService {

    private com.staticvoid.userProfile.repository.UserProfileRepository userProfileRepository;
    private PostService postService;

    public UserProfileDto getUserProfile(ApplicationUserDto user, Pageable pageable) {
        UserProfile userProfile = userProfileRepository.findByUser(user.toEntityNotRecursive());
        UserProfileDto userProfileDto = UserProfileDto.toDto(userProfile);
        userProfileDto.setPosts(postService.getUserPosts(user, pageable));
        return userProfileDto;
    }
}
