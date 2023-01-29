package com.staticvoid.post.domain;

import com.staticvoid.image.domain.ImageDto;
import com.staticvoid.post.comment.domain.CommentDto;
import com.staticvoid.songsuggestion.domain.SongDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDto implements Serializable {

    private Long id;
    private ImageDto image;
    private SongDto song;
    private String content;
    private List<CommentDto> comments;
    private Date date;
    private Long likes;

    public static PostDto toDto(Post entity) {
        PostDto dto = new PostDto();
        dto.setId(entity.getId());
        dto.setImage(ImageDto.toDto(entity.getImage()));
        dto.setSong(SongDto.toDto(entity.getSong()));
        dto.setContent(entity.getContent());
        dto.setComments(CommentDto.toDto(entity.getComments()));
        dto.setDate(entity.getDate());
        dto.setLikes(entity.getLikes());
        return dto;
    }

    public Post toEntity() {
        Post post = new Post();
        post.setId(id);
        post.setImage(image.toEntity());
        post.setSong(song.toEntity());
        post.setContent(content);
        post.setComments(comments.stream().map(CommentDto::toEntity).collect(Collectors.toList()));
        post.setDate(date);
        post.setLikes(likes);
        return post;
    }
}
