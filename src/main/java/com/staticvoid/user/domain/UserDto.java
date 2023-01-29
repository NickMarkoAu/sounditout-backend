package com.staticvoid.user.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private String id;

    private String name;
    private String email;
    private Date dateOfBirth;
    private Long tokens;
    private List<UserDto> followers;
    private List<UserDto> following;

    public User toEntity() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setDateOfBirth(dateOfBirth);
        user.setTokens(tokens);
        user.setFollowers(followers.stream().map(UserDto::toEntityNotRecursive).collect(Collectors.toList()));
        user.setFollowing(following.stream().map(UserDto::toEntityNotRecursive).collect(Collectors.toList()));
        return user;
    }

    public static UserDto toDto(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setTokens(entity.getTokens());
        dto.setFollowers(entity.getFollowers().stream().map(UserDto::toDtoNotRecursive).collect(Collectors.toList()));
        dto.setFollowing(entity.getFollowing().stream().map(UserDto::toDtoNotRecursive).collect(Collectors.toList()));
        return dto;
    }

    public User toEntityNotRecursive() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setDateOfBirth(dateOfBirth);
        user.setTokens(tokens);
        return user;
    }

    public static UserDto toDtoNotRecursive(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setTokens(entity.getTokens());
        return dto;
    }

}
