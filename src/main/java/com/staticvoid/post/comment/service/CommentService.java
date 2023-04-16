package com.staticvoid.post.comment.service;

import com.staticvoid.post.comment.domain.Comment;
import com.staticvoid.post.comment.domain.dto.CommentDto;
import com.staticvoid.post.comment.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;

    private static final int COMMENTS_ON_FEED = 3;

    public CommentDto createComment(CommentDto comment) {
        comment.setDate(new Date());
        return CommentDto.toDto(commentRepository.save(comment.toEntity()));
    }

    public Page<CommentDto> findPaginatedCommentsByPostId(Long postId, Pageable pageable) {
        List<Comment> commentList = commentRepository.findByPostId(postId, pageable).getContent();
        return new PageImpl<>(CommentDto.toDto(commentList), pageable, commentList.size());
    }

    public Map<Long, List<Comment>> getPaginatedCommentsForPosts(List<Long> postIds) {
        List<Comment> comments = commentRepository.findPaginatedCommentsForMultiplePosts(postIds, COMMENTS_ON_FEED, COMMENTS_ON_FEED);

        return comments.stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getPost().getId()
                ));
    }
}
