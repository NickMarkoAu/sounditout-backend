package com.staticvoid.post.repository;

import com.staticvoid.post.domain.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    Optional<SavedPost> findByPostIdAndUserId(Long postId, Long userId);
}
