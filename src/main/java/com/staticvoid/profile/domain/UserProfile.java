package com.staticvoid.userProfile.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    private String bio;

    @OneToOne
    @JoinColumn(name = "song_id")
    private Song headlineSong;

    @Transient
    public Long getFollowersCount() {
        if (user != null) {
            return (long) user.getFollowers().size();
        }
        return 0L;
    }

    @Transient
    public Long getFollowingCount() {
        if (user != null) {
            return (long) user.getFollowing().size();
        }
        return 0L;
    }

    @Transient
    public Long getPostsCount() {
        if (user != null && user.getPosts() != null) {
            return (long) user.getPosts().size();
        }
        return 0L;
    }
}
