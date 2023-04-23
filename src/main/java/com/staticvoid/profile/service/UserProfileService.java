package com.staticvoid.profile.service;

import com.staticvoid.post.service.PostService;
import com.staticvoid.profile.repository.UserProfileRepository;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.service.ApplicationUserService;
import com.staticvoid.userProfile.domain.UserProfile;
import com.staticvoid.userProfile.domain.dto.UserProfileDto;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class UserProfileService {

    private UserProfileRepository userProfileRepository;
    private PostService postService;
    private ApplicationUserService applicationUserService;

    @Transactional
    public UserProfileDto getUserProfile(ApplicationUserDto user, Pageable pageable) {
        ApplicationUser appUser = user.toEntityNotRecursive();
        UserProfile userProfile = userProfileRepository.findByUser(appUser);
        UserProfileDto userProfileDto = UserProfileDto.toDto(userProfile);
        userProfileDto.setPosts(postService.getUserPosts(user, pageable).getContent());
        userProfileDto.setPostsCount(postService.getPostCountForUser(appUser));
        return userProfileDto;
    }

    public UserProfileDto followUser(ApplicationUserDto user) {
        ApplicationUser loggedInUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser followingUser = applicationUserService.loadUserById(user.getId());
        loggedInUser.getFollowing().add(followingUser);
        followingUser.getFollowers().add(loggedInUser);
        applicationUserService.save(loggedInUser);
        applicationUserService.save(followingUser);

        UserProfile userProfile = userProfileRepository.findByUser(followingUser);
        UserProfileDto userProfileDto = UserProfileDto.toDto(userProfile);
        userProfileDto.setPostsCount(postService.getPostCountForUser(followingUser));
        return userProfileDto;
    }
}
