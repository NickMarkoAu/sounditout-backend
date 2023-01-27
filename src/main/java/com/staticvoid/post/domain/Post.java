package com.staticvoid.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staticvoid.image.domain.Image;
import com.staticvoid.songsuggestion.domain.Song;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Image image;

    @OneToOne
    @JoinColumn(name = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Song song;


}
