package com.staticvoid.post.domain;

import com.staticvoid.image.domain.Image;
import com.staticvoid.image.domain.ImageDto;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.SongDto;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

public class PostDto implements Serializable {

    private Long id;
    private ImageDto image;
    private SongDto song;
}
