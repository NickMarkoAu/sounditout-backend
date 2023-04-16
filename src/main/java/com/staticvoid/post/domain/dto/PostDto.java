package com.staticvoid.post.domain.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.image.domain.dto.ImageDto;
import com.staticvoid.post.comment.domain.dto.CommentDto;
import com.staticvoid.post.domain.Post;
import com.staticvoid.post.domain.PostPrivacy;
import com.staticvoid.songsuggestion.domain.dto.SongDto;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
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

    public static PostDto toDto(Post entity, List<CommentDto> comments) {
        ObjectMapper mapper = new ObjectMapper();
        PostDto dto = new PostDto();
        dto.setId(entity.getId());
        dto.setImage(ImageDto.toDto(entity.getImage()));
        dto.setSong(SongDto.toDto(entity.getSong()));
        dto.setContent(entity.getContent());
        dto.setComments(comments);
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

    public static PostDto toDtoNoComments(Post entity) {
        ObjectMapper mapper = new ObjectMapper();
        PostDto dto = new PostDto();
        dto.setId(entity.getId());
        dto.setImage(ImageDto.toDto(entity.getImage()));
        dto.setSong(SongDto.toDto(entity.getSong()));
        dto.setContent(entity.getContent());
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
        post.setTags(JSONArray.toJSONString(Arrays.asList(tags)));
        post.setUser(user.toEntityNotRecursive());
        return post;
    }

    public Post toEntityNoComments() throws ParseException {
        Post post = new Post();
        post.setId(id);
        post.setImage(image.toEntity());
        post.setSong(song.toEntity());
        post.setContent(content);
        post.setDate(sdf.parse(date));
        post.setLikes(likes);
        post.setPrivacy(privacy);
        post.setTags(JSONArray.toJSONString(Arrays.asList(tags)));
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
