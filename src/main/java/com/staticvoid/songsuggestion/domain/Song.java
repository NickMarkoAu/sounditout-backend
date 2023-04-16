package com.staticvoid.songsuggestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long imageId;
    private String userId;
    private String name;
    private String artist;
    private String tags;

    public Song(Long id, Long imageId, String name, String artist, String tags) {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
        this.artist = artist;
        this.tags = tags;
    }

    public String toPromptString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
