package com.staticvoid.songsuggestion.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateResultDto implements Serializable {
    private final SongDto[] songs;
    private final String[] tags;
    private final String error;

    public GenerateResultDto(SongDto[] songs, String[] tags) {
        this.songs = songs;
        this.tags = tags;
        this.error = null;
    }

    public GenerateResultDto(String error) {
        this.error = error;
        this.songs = new SongDto[0];
        this.tags = new String[0];
    }
}
