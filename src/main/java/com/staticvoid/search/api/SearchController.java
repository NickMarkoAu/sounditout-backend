package com.staticvoid.search.api;

import com.staticvoid.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/api/search/users/{query}")
    public ResponseEntity<?> searchUsers(@PathVariable("query") String query, Pageable pageable) {
        try {
            return ResponseEntity.ok(searchService.searchUsers(query, pageable));
        } catch(Exception e) {
            log.error("Error searching users", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/api/search/posts/{query}")
    public ResponseEntity<?> searchPosts(@PathVariable("query") String query, Pageable pageable) {
        try {
            return ResponseEntity.ok(searchService.searchPosts(query, pageable));
        } catch(Exception e) {
            log.error("Error searching posts", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/api/search/music/{query}")
    public ResponseEntity<?> searchMusic(@PathVariable("query") String query, Pageable pageable) {
        try {
            return ResponseEntity.ok(searchService.searchMusic(query, pageable));
        } catch(Exception e) {
            log.error("Error searching music", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
