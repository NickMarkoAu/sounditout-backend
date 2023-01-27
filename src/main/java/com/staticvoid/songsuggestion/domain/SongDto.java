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

    public static SongDto toDto(Song song) {
        ObjectMapper mapper = new ObjectMapper();
        SongDto songDto = new SongDto();
        songDto.id = song.getId();
        songDto.imageId = song.getImageId();
        songDto.userId = song.getUserId();
        songDto.name = song.getName();
        songDto.artist = song.getArtist();
        try {
            songDto.tags = mapper.readValue(song.getTags(), String[].class);
        } catch(Exception e) {
            throw new RuntimeException("Could not deserialise tags");
        }
        songDto.youtubeVideoId = song.getYoutubeVideoId();
        return songDto;
    }
}
