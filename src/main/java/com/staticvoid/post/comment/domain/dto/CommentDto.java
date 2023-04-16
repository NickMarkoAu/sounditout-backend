package com.staticvoid.post.comment.domain.dto;

import com.staticvoid.post.comment.domain.Comment;
import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
public class CommentDto implements Serializable {
    private Long id;
    private ApplicationUserDto user;
    private String content;
    private PostDto post;
    private Date date;

    public static CommentDto toDto(Comment entity) {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setUser(ApplicationUserDto.toDtoNotRecursive(entity.getApplicationUser()));
        dto.setDate(entity.getDate());
        dto.setPost(PostDto.toDtoNoComments(entity.getPost()));
        return dto;
    }

    public static List<CommentDto> toDto(List<Comment> entity) {
        return entity.stream().map(CommentDto::toDto).collect(Collectors.toList());
    }

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        comment.setApplicationUser(user.toEntityNotRecursive());
        comment.setDate(date);
        try {
            comment.setPost(post.toEntity());
        } catch(Exception e) {
            log.warn("Could not set post for comment", e);
        }

        return comment;
    }
}
