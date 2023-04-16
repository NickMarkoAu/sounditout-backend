package com.staticvoid.post.comment.api;

import com.staticvoid.post.comment.domain.dto.CommentDto;
import com.staticvoid.post.comment.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comment/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDto comment) {
        try {
            CommentDto createdComment = commentService.createComment(comment);
            return ResponseEntity.ok(createdComment);
        } catch(Exception e) {
            log.error("Error creating comment", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/api/comment/{postId}")
    public ResponseEntity<?> getAllCommentsForPost(@PathVariable Long postId, Pageable pageable) {
        try {
            Page<CommentDto> comments = commentService.findPaginatedCommentsByPostId(postId, pageable);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            log.error("Error getting comments", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
