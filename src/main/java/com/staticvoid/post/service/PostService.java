package com.staticvoid.post.service;

import com.staticvoid.post.domain.PostDto;
import com.staticvoid.post.repository.PostRepository;
import com.staticvoid.user.domain.User;
import com.staticvoid.user.domain.UserDto;
import com.staticvoid.user.respository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    /**
     * Gets the posts of the users that the requesting user follows. Ordered by date (newest first) and pageable
     * May make this take into account the popularity of the post later.
     * @param user
     * @param pageable
     * @return
     */
    public Page<PostDto> getFeedPostsForUser(UserDto user, Pageable pageable) {
        List<User> following = userRepository.findById(user.getId()).orElseThrow().getFollowing();
        List<PostDto> posts = postRepository.findByUsers(following, pageable).stream().map(PostDto::toDto).collect(Collectors.toList());
        return new PageImpl<>(posts, pageable, posts.size());
    }



}
