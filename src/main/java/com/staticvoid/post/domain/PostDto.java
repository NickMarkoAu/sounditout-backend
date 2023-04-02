package com.staticvoid.post.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.image.domain.ImageDto;
import com.staticvoid.post.comment.domain.CommentDto;
import com.staticvoid.songsuggestion.domain.SongDto;
import com.staticvoid.user.domain.ApplicationUserDto;
import lombok.Data;
import net.minidev.json.JSONArray;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
    private String date;
    private Long likes;
    private PostPrivacy privacy;
    private String[] tags;
    private ApplicationUserDto user;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

    public static PostDto toDto(Post entity) {
        ObjectMapper mapper = new ObjectMapper();
        PostDto dto = new PostDto();
        dto.setId(entity.getId());
        dto.setImage(ImageDto.toDto(entity.getImage()));
        dto.setSong(SongDto.toDto(entity.getSong()));
        dto.setContent(entity.getContent());
        if(entity.getComments() != null) {
            dto.setComments(CommentDto.toDto(entity.getComments()));
        }
        dto.setDate(sdf.format(entity.getDate()));
        if(entity.getLikes() != null) {
            dto.setLikes(entity.getLikes());
        }
        dto.setPrivacy(entity.getPrivacy());
        dto.setUser(ApplicationUserDto.toDtoNotSensitiveNotRecursive(entity.getUser()));
        try {
            dto.setTags(mapper.readValue(entity.getTags(), String[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not read tags value from Post entity", e);
        }
        return dto;
    }

    public Post toEntity() throws ParseException {
        Post post = new Post();
        post.setId(id);
        post.setImage(image.toEntity());
        post.setSong(song.toEntity());
        post.setContent(content);
        post.setComments(comments.stream().map(CommentDto::toEntity).collect(Collectors.toList()));
        post.setDate(sdf.parse(date));
        post.setLikes(likes);
        post.setPrivacy(privacy);
        post.setTags(Arrays.toString(tags));
        post.setUser(user.toEntityNotRecursive());
        return post;
    }

    public Post toNewPostEntity() {
        Post post = new Post();
        post.setId(id);
        post.setImage(image.toEntity());
        post.setTags(JSONArray.toJSONString(Arrays.asList(image.getTags())));
        post.setSong(song.toEntity());
        post.setContent(content);
        post.setDate(new Date());
        post.setPrivacy(privacy);
        post.setUser(user.toEntityNotRecursive());
        return post;
    }
}
