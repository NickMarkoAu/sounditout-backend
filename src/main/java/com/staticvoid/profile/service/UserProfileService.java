package com.staticvoid.profile.service;

import com.staticvoid.image.domain.Image;
import com.staticvoid.post.service.PostService;
import com.staticvoid.profile.repository.UserProfileRepository;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.service.ApplicationUserService;
import com.staticvoid.profile.domain.UserProfile;
import com.staticvoid.profile.domain.dto.UserProfileDto;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.util.PageableUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    @Transactional
    public UserProfileDto updateUserProfile(UserProfileDto userProfile) {
        ApplicationUser user = applicationUserService.loadUserById(userProfile.getUser().getId());
        UserProfile userProfileEntity = userProfileRepository.findByUser(user);
        if(userProfile.getBio() != null) {
            userProfileEntity.setBio(userProfile.getBio());
        }
        if(userProfile.getHeadlineSong() != null) {
            userProfileEntity.setHeadlineSong(userProfile.getHeadlineSong().toEntity());
        }
        if(userProfile.getUser() != null && userProfile.getUser().getName() != null) {
            user.setName(userProfile.getUser().getName());
        }
        applicationUserService.save(user);
        userProfileRepository.save(userProfileEntity);
        return getUserProfile(userProfile.getUser(), PageableUtil.DEFAULT_PAGEABLE);
    }

    public UserProfileDto followUser(ApplicationUserDto user) {
        ApplicationUser loggedInUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser followingUser = applicationUserService.loadUserById(user.getId());
        if(loggedInUser.getFollowing().contains(followingUser)) {
            loggedInUser.getFollowing().remove(followingUser);
            followingUser.getFollowers().remove(loggedInUser);
            applicationUserService.save(followingUser);
            applicationUserService.save(loggedInUser);

            UserProfileDto userProfileDto = UserProfileDto.toDto(userProfileRepository.findByUser(followingUser));
            userProfileDto.setPostsCount(postService.getPostCountForUser(followingUser));
            return userProfileDto;
        }
        loggedInUser.getFollowing().add(followingUser);
        followingUser.getFollowers().add(loggedInUser);
        applicationUserService.save(followingUser);
        applicationUserService.save(loggedInUser);

        UserProfile userProfile = userProfileRepository.findByUser(followingUser);
        UserProfileDto userProfileDto = UserProfileDto.toDto(userProfile);
        userProfileDto.setPostsCount(postService.getPostCountForUser(followingUser));
        return userProfileDto;
    }

    public UserProfileDto updateProfilePicture(Image profilePicture) {
        ApplicationUser loggedInUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser user = applicationUserService.loadUserById(loggedInUser.getId());
        user.setProfileImage(profilePicture);
        applicationUserService.save(user);

        UserProfile userProfile = userProfileRepository.findByUser(user);
        UserProfileDto userProfileDto = UserProfileDto.toDto(userProfile);
        userProfileDto.setPostsCount(postService.getPostCountForUser(user));
        userProfileDto.setPosts(postService.getUserPosts(ApplicationUserDto.toDtoNotRecursive(user), PageableUtil.DEFAULT_PAGEABLE).getContent());
        return userProfileDto;
    }

    public Page<UserProfile> search(String query, Pageable pageable) {
        return userProfileRepository.search(query, pageable);
    }
}
