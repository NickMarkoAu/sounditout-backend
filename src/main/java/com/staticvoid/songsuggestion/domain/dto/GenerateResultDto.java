package com.staticvoid.songsuggestion.domain.dto;

import com.staticvoid.image.domain.dto.ImageDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateResultDto implements Serializable {
    private final SongDto[] songs;
    private final String[] tags;
    private final String error;
    private final ImageDto image;

    public GenerateResultDto(SongDto[] songs, String[] tags, ImageDto image) {
        this.songs = songs;
        this.tags = tags;
        this.image = image;
        this.error = null;
    }

    public GenerateResultDto(String error) {
        this.error = error;
        this.songs = new SongDto[0];
        this.tags = new String[0];
        this.image = new ImageDto();
    }
}
