package com.staticvoid.user.domain;

import com.staticvoid.image.domain.Image;
import com.staticvoid.post.domain.Post;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class ApplicationUser implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private Date dateOfBirth;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_image_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Image profileImage;

    @OneToOne
    private ApplicationUserTokens tokens;
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ApplicationUser> followers;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ApplicationUser> following;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
