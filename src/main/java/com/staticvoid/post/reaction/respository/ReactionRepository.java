package com.staticvoid.post.reaction.respository;

import com.staticvoid.post.reaction.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    Optional<Reaction> findByPostIdAndUserId(Long postId, Long userId);
}
