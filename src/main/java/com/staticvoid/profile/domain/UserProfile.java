package com.staticvoid.profile.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserProfile that = (UserProfile) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
