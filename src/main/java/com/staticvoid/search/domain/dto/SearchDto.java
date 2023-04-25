package com.staticvoid.search.domain.dto;

import com.staticvoid.search.domain.Search;
import com.staticvoid.search.domain.SearchResult;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class SearchDto implements Serializable {
    private String query;
    private Date date;
    private Page<? extends SearchResult> results;
    private SearchType type;

    public static SearchDto toDto(Search entity) {
        return new SearchDtoBuilder()
                .date(entity.getDate())
                .query(entity.getQuery())
                .type(entity.getType())
                .build();
    }

    public enum SearchType {
        POST,
        MUSIC,
        USER
    }
}
