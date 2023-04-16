package com.staticvoid.post.comment.domain;

import com.staticvoid.post.domain.Post;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.Data;
import org.hibernate.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private ApplicationUser applicationUser;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Post post;

    private Date date;
}
