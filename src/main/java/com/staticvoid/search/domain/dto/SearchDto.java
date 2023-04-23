package com.staticvoid.search.domain.dto;

import com.staticvoid.search.domain.Search;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SearchDto implements Serializable {
    private String searchQuery;
    private Date date;
    private ApplicationUserDto user;

    public static SearchDto toDto(Search entity) {
        SearchDto dto = new SearchDto();
        dto.setSearchQuery(entity.getSearchQuery());
        dto.setDate(entity.getDate());
        dto.setUser(ApplicationUserDto.toDtoNotSensitiveNotRecursive(entity.getUser()));
        return dto;
    }
}
