package com.staticvoid.post.comment.domain;

import com.staticvoid.user.domain.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;

@Entity
@Data
public class Comment implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    private User user;

    private String content;

}
