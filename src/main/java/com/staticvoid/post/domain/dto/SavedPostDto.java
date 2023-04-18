package com.staticvoid.post.domain.dto;

import com.staticvoid.post.domain.SavedPost;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.Data;

@Data
public class SavedPostDto {
    private Long id;
    private PostDto post;
    private ApplicationUserDto user;

    public static SavedPostDto toDto(SavedPost entity) {
        SavedPostDto savedPostDto = new SavedPostDto();
        savedPostDto.setPost(PostDto.toDtoNoComments(entity.getPost()));
        savedPostDto.setUser(ApplicationUserDto.toDtoNotRecursive(entity.getUser()));
        return savedPostDto;
    }
}
