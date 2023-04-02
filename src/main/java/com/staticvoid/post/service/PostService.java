package com.staticvoid.post.service;

import com.staticvoid.post.domain.PostDto;
import com.staticvoid.post.repository.PostRepository;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.ApplicationUserDto;
import com.staticvoid.user.respository.ApplicationUserRepository;
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
    private ApplicationUserRepository applicationUserRepository;

    /**
     * Gets the posts of the users that the requesting user follows. Ordered by date (newest first) and pageable
     * May make this take into account the popularity of the post later.
     * @param user
     * @param pageable
     * @return
     */
    public Page<PostDto> getFeedPostsForUser(ApplicationUserDto user, Pageable pageable) {
        List<ApplicationUser> following = applicationUserRepository.findById(user.getId()).orElseThrow().getFollowing();
        //include posts by own user
        following.add(user.toEntityNotRecursive());
        List<Long> userIds = following.stream().map(ApplicationUser::getId).collect(Collectors.toList());
        List<PostDto> posts = postRepository.findByUsers(userIds, pageable).stream().map(PostDto::toDto).collect(Collectors.toList());
        return new PageImpl<>(posts, pageable, posts.size());
    }

    public PostDto createPost(PostDto post) {
        return PostDto.toDto(postRepository.save(post.toNewPostEntity()));
    }



}
