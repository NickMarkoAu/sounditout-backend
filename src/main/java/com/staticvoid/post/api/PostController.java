package com.staticvoid.post.api;

import com.staticvoid.post.domain.PostDto;
import com.staticvoid.post.service.PostService;
import com.staticvoid.user.domain.ApplicationUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts/feed")
    public ResponseEntity<?> getFeedPostsForUser(@RequestBody ApplicationUserDto user, Pageable pageable) {
        try {
            Page<PostDto> posts = postService.getFeedPostsForUser(user, pageable);
            return ResponseEntity.ok(posts);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
