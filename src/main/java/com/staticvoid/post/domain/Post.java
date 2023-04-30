package com.staticvoid.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staticvoid.image.domain.Image;
import com.staticvoid.post.comment.domain.Comment;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
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
import java.util.Objects;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private List<Comment> comments;

    private Date date;

    private Long likes;

    private PostPrivacy privacy;

    private boolean draft;

    private String tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Post post = (Post) o;
        return id != null && Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

