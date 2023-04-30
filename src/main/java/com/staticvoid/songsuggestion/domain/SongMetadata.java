package com.staticvoid.songsuggestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SongMetadata implements Serializable {

    public SongMetadata(Song song, String spotifyUrl, String previewUrl, String albumArtUrl) {
        this.song = song;
        this.spotifyUrl = spotifyUrl;
        this.previewUrl = previewUrl;
        this.albumArtUrl = albumArtUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "song_id")
    private Song song;

    private String spotifyUrl;
    private String previewUrl;
    private String albumArtUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SongMetadata metadata = (SongMetadata) o;
        return id != null && Objects.equals(id, metadata.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
