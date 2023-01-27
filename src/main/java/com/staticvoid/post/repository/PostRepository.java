package com.staticvoid.post.repository;

import com.staticvoid.image.domain.Image;
import com.staticvoid.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
