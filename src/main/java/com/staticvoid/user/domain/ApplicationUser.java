package com.staticvoid.user.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class ApplicationUser implements Serializable {
    @Id
    private String id;

    private String name;
    private String email;
    private Date dateOfBirth;
    private Long tokens;
    private String password;

    @OneToMany
    private List<ApplicationUser> followers;

    @OneToMany
    private List<ApplicationUser> following;
}
