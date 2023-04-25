package com.staticvoid.search.api;

import com.staticvoid.search.domain.SearchResult;
import com.staticvoid.search.domain.dto.SearchDto;
import com.staticvoid.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/api/search/recent/{userId}")
    public ResponseEntity<?> getRecentSearchesForUser(@PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(searchService.getRecentSearches(userId));
        } catch(Exception e) {
            log.error("Error getting recent searches", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/api/search/{type}/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, @PathVariable("type") SearchDto.SearchType type, Pageable pageable) {
        try {
            Page<? extends SearchResult> searchResults = null;
            if(type.equals(SearchDto.SearchType.USER)) {
                searchResults = searchService.searchUsers(query, pageable);
            } else if (type.equals(SearchDto.SearchType.POST)) {
                searchResults = searchService.searchPosts(query, pageable);
            } else if (type.equals(SearchDto.SearchType.MUSIC)) {
                searchResults = searchService.searchMusic(query, pageable);
            }

            SearchDto result = SearchDto.builder()
                    .query(query)
                    .type(type)
                    .results(searchResults)
                    .build();

            return ResponseEntity.ok(result);
        } catch(Exception e) {
            log.error("Error searching", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
