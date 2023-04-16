package com.staticvoid.user.domain.dto;

import com.staticvoid.image.domain.dto.ImageDto;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ApplicationUserDto implements Serializable {
    private Long id;

    private String name;
    private String email;
    private Date dateOfBirth;
    private ImageDto profileImage;
    private ApplicationUserTokensDto tokens;
    private List<ApplicationUserDto> followers;
    private List<ApplicationUserDto> following;
    private String password;

    public ApplicationUser toEntity() {
        ApplicationUser applicationUser = toEntityNotRecursive();
        applicationUser.setFollowers(followers.stream().map(ApplicationUserDto::toEntityNotRecursive).collect(Collectors.toList()));
        applicationUser.setFollowing(following.stream().map(ApplicationUserDto::toEntityNotRecursive).collect(Collectors.toList()));
        return applicationUser;
    }

    public static ApplicationUserDto toDto(ApplicationUser entity) {
        ApplicationUserDto dto = ApplicationUserDto.toDtoNotRecursive(entity);
        dto.setFollowers(entity.getFollowers().stream().map(ApplicationUserDto::toDtoNotRecursive).collect(Collectors.toList()));
        dto.setFollowing(entity.getFollowing().stream().map(ApplicationUserDto::toDtoNotRecursive).collect(Collectors.toList()));
        return dto;
    }

    public ApplicationUser toEntityNotRecursive() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(id);
        applicationUser.setName(name);
        applicationUser.setEmail(email);
        applicationUser.setDateOfBirth(dateOfBirth);
        applicationUser.setProfileImage(profileImage.toEntity());
        if(tokens != null) {
            applicationUser.setTokens(tokens.toEntity());
        }
        return applicationUser;
    }

    public static ApplicationUserDto toDtoNotRecursive(ApplicationUser entity) {
        ApplicationUserDto dto = new ApplicationUserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setProfileImage(ImageDto.toDto(entity.getProfileImage()));
        dto.setTokens(ApplicationUserTokensDto.toDto(entity.getTokens()));
        return dto;
    }

    public static ApplicationUserDto toDtoNotSensitiveNotRecursive(ApplicationUser entity) {
        ApplicationUserDto dto = new ApplicationUserDto();
        dto.setId(entity.getId());
        dto.setProfileImage(ImageDto.toDto(entity.getProfileImage()));
        dto.setName(entity.getName());
        return dto;
    }

}
