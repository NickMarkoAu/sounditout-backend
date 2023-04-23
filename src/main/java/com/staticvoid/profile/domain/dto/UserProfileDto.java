package com.staticvoid.userProfile.domain.dto;

import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.userProfile.domain.UserProfile;
import com.staticvoid.songsuggestion.domain.dto.SongDto;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserProfileDto implements Serializable {
    private ApplicationUserDto user;
    private Long followersCount;
    private Long followingCount;
    private Long postsCount;
    private List<PostDto> posts;
    private SongDto headlineSong;
    private String bio;
    private boolean isOwn;
    private boolean isFollowing;

    public static UserProfileDto toDto(UserProfile userProfile) {
        UserProfileDto userProfileDto = new UserProfileDto();
        ApplicationUser loggedInUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // If the user is viewing their own userProfile, send all the data
        if(loggedInUser.getId().equals(userProfile.getUser().getId())) {
            userProfileDto.setUser(ApplicationUserDto.toDtoNotRecursive(userProfile.getUser()));
            userProfileDto.setOwn(true);
        } else {
            userProfileDto.setUser(ApplicationUserDto.toDtoNotSensitiveNotRecursive(userProfile.getUser()));
            userProfileDto.setOwn(false);
            userProfileDto.setFollowing(loggedInUser.getFollowing().stream().map(ApplicationUser::getId).collect(Collectors.toList()).contains(userProfile.getUser().getId()));
        }
        userProfileDto.setFollowersCount(userProfile.getFollowersCount());
        userProfileDto.setFollowingCount(userProfile.getFollowingCount());
        userProfileDto.setHeadlineSong(SongDto.toDto(userProfile.getHeadlineSong()));
        userProfileDto.setBio(userProfile.getBio());
        return userProfileDto;
    }
}
