package com.staticvoid.post.comment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentDto implements Serializable {
    private String id;
    private String content;

    public static CommentDto toDto(Comment entity) {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        return dto;
    }

    public static List<CommentDto> toDto(List<Comment> entity) {
        return entity.stream().map(CommentDto::toDto).collect(Collectors.toList());
    }

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        return comment;
    }
}
