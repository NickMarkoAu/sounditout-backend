package com.staticvoid.post.service;

import com.staticvoid.post.comment.domain.Comment;
import com.staticvoid.post.comment.domain.dto.CommentDto;
import com.staticvoid.post.comment.service.CommentService;
import com.staticvoid.post.domain.Post;
import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.post.repository.PostRepository;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.user.respository.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PostService {

    private PostRepository postRepository;
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private CommentService commentService;

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
        Page<Post> posts = postRepository.findByUsers(userIds, pageable);

        List<Long> postIds = posts.getContent().stream().map(Post::getId).collect(Collectors.toList());
        Map<Long, List<Comment>> postIdToCommentsMap = commentService.getPaginatedCommentsForPosts(postIds);

        List<PostDto> postDtos = posts.getContent().stream()
                .map(post -> {
                    List<Comment> comments = postIdToCommentsMap.getOrDefault(post.getId(), Collections.emptyList());
                    return PostDto.toDto(post, CommentDto.toDto(comments));
                }).collect(Collectors.toList());

        return new PageImpl<>(postDtos, pageable, posts.getTotalElements());
    }

    public PostDto createPost(PostDto post) {
        return PostDto.toDto(postRepository.save(post.toNewPostEntity()), List.of());
    }

    public Integer getCommentCountForPost(Long postId) {
        return postRepository.findById(postId).orElseThrow().getComments().size();
    }

}
