package com.staticvoid.post.reaction.domain;

import com.staticvoid.post.domain.Post;
import com.staticvoid.post.reaction.domain.dto.ReactionDto;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private ApplicationUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Post post;

    private ReactionDto.ReactionType reactionType;
}
