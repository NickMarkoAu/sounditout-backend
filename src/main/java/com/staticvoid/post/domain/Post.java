package com.staticvoid.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staticvoid.image.domain.Image;
import com.staticvoid.post.comment.domain.Comment;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ApplicationUser user;

    @OneToOne
    @JoinColumn(name = "image_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Image image;

    @OneToOne
    @JoinColumn(name = "song_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Song song;

    private String content;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> comments;

    private Date date;

    private Long likes;

    private PostPrivacy privacy;

    private boolean draft;

    private String tags;

}

