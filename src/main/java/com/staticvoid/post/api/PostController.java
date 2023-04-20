package com.staticvoid.post.api;

import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.post.domain.dto.SavedPostDto;
import com.staticvoid.post.service.PostService;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts/feed")
    public ResponseEntity<?> getFeedPostsForUser(@RequestBody ApplicationUserDto user, Pageable pageable) {
        try {
            Page<PostDto> posts = postService.getFeedPostsForUser(user, pageable);
            return ResponseEntity.ok(posts);
        } catch(Exception e) {
            log.error("Error getting posts", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/api/posts/user")
    public ResponseEntity<?> getUserPosts(@RequestBody ApplicationUserDto user, Pageable pageable) {
        try {
            Page<PostDto> posts = postService.getUserPosts(user, pageable);
            return ResponseEntity.ok(posts);
        } catch(Exception e) {
            log.error("Error getting posts", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/api/posts/create")
    public ResponseEntity<?> createPost(@RequestBody PostDto post) {
        try {
            PostDto createdPost = postService.createPost(post);
            return ResponseEntity.ok(createdPost);
        } catch(Exception e) {
            log.error("Error creating post", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/api/posts/{postId}/comments/count")
    public Integer getCommentCountForPost(@PathVariable Long postId) {
        return postService.getCommentCountForPost(postId);
    }

    @PostMapping("/api/posts/save")
    public ResponseEntity<?> savePost(@RequestBody SavedPostDto post) {
        try {
            if(post.getPost().isSaved()) {
                SavedPostDto savedPost = postService.userUnsavePost(post.getPost().getId(), post.getUser().getId());
                return ResponseEntity.ok(savedPost);
            } else {
                if(!postService.hasUserSavedPost(post.getUser().getId(), post.getPost().getId())) {
                    SavedPostDto savedPost = postService.userSavePost(post.getPost().getId(), post.getUser().getId());
                    return ResponseEntity.ok(savedPost);
                }
            }
            return ResponseEntity.ok(post);
        } catch(Exception e) {
            log.error("Error saving post", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
