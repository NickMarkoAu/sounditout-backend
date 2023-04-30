package com.staticvoid.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staticvoid.search.domain.dto.SearchDto;
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
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Objects;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String query;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;

    private SearchDto.SearchType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Search search = (Search) o;
        return id != null && Objects.equals(id, search.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
