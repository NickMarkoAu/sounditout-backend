package com.staticvoid.songsuggestion.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

@Data
public class SongDto implements Serializable {
    private Long id;
    private String imageId;
    private String userId;
    private String name;
    private String artist;
    private String[] tags;
    private String youtubeVideoId;

    public Song toEntity() {
        Song song = new Song();
        song.setId(id);
        song.setImageId(imageId);
        song.setUserId(userId);
        song.setName(name);
        song.setArtist(artist);
        song.setTags(Arrays.toString(tags));
        song.setYoutubeVideoId(youtubeVideoId);
        return song;
    }

    private void toDto(Song song) {
        ObjectMapper mapper = new ObjectMapper();

        this.id = song.getId();
        this.imageId = song.getImageId();
        this.userId = song.getUserId();
        this.name = song.getName();
        this.artist = song.getArtist();
        try {
            this.tags = mapper.readValue(song.getTags(), String[].class);
        } catch(Exception e) {
            throw new RuntimeException("Could not deserialise tags");
        }
        this.youtubeVideoId = song.getYoutubeVideoId();
    }
}
