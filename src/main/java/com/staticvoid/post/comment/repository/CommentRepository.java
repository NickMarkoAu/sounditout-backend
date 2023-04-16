package com.staticvoid.post.comment.repository;

import com.staticvoid.post.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    @Query(value = "SELECT * FROM comment WHERE post_id IN :postIds AND id IN (" +
            "SELECT id FROM comment c1 WHERE c1.post_id = comment.post_id " +
            "ORDER BY c1.date DESC LIMIT :size OFFSET :offset)",
            nativeQuery = true)
    List<Comment> findPaginatedCommentsForMultiplePosts(@Param("postIds") List<Long> postIds, @Param("offset") int offset, @Param("size") int size);
}
