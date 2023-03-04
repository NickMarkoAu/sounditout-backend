package com.staticvoid.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staticvoid.image.domain.Image;
import com.staticvoid.post.comment.domain.Comment;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
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
    private Long id;

    @ManyToOne
    private User user;

    @OneToOne
    @JoinColumn(name = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Image image;

    @OneToOne
    @JoinColumn(name = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Song song;

    private String content;

    @OneToMany
    private List<Comment> comments;

    private Date date;

    private Long likes;

    private PostPrivacy privacy;

}

