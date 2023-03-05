package com.staticvoid.user.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ApplicationUserDto {
    private String id;

    private String name;
    private String email;
    private Date dateOfBirth;
    private Long tokens;
    private List<ApplicationUserDto> followers;
    private List<ApplicationUserDto> following;
    private String password;

    public ApplicationUser toEntity() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(id);
        applicationUser.setName(name);
        applicationUser.setEmail(email);
        applicationUser.setDateOfBirth(dateOfBirth);
        applicationUser.setTokens(tokens);
        applicationUser.setFollowers(followers.stream().map(ApplicationUserDto::toEntityNotRecursive).collect(Collectors.toList()));
        applicationUser.setFollowing(following.stream().map(ApplicationUserDto::toEntityNotRecursive).collect(Collectors.toList()));
        return applicationUser;
    }

    public static ApplicationUserDto toDto(ApplicationUser entity) {
        ApplicationUserDto dto = new ApplicationUserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setTokens(entity.getTokens());
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
        applicationUser.setTokens(tokens);
        return applicationUser;
    }

    public static ApplicationUserDto toDtoNotRecursive(ApplicationUser entity) {
        ApplicationUserDto dto = new ApplicationUserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setTokens(entity.getTokens());
        return dto;
    }

}
