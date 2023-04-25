package com.staticvoid.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.staticvoid.search.domain.dto.SearchDto;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
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

}
