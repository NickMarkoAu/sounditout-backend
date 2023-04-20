package com.staticvoid.post.service;

import com.staticvoid.post.comment.domain.Comment;
import com.staticvoid.post.comment.domain.dto.CommentDto;
import com.staticvoid.post.comment.service.CommentService;
import com.staticvoid.post.domain.Post;
import com.staticvoid.post.domain.SavedPost;
import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.post.domain.dto.SavedPostDto;
import com.staticvoid.post.reaction.service.ReactionService;
import com.staticvoid.post.repository.PostRepository;
import com.staticvoid.post.repository.SavedPostRepository;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.user.respository.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private CommentService commentService;
    private ReactionService reactionService;
    private SavedPostRepository savedPostRepository;

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
        //This is going to return all public posts and all following posts.
        // We need to work out an algorithm to return the ones that the user actually wants to see
        Page<Post> posts = postRepository.findByUsers(userIds, pageable);

        List<Long> postIds = posts.getContent().stream().map(Post::getId).collect(Collectors.toList());
        Map<Long, List<Comment>> postIdToCommentsMap = commentService.getPaginatedCommentsForPosts(postIds);

        List<PostDto> postDtos = posts.getContent().stream()
                .map(post -> {
                    List<Comment> comments = postIdToCommentsMap.getOrDefault(post.getId(), Collections.emptyList());
                    PostDto postDto =  PostDto.toDto(post, CommentDto.toDto(comments));
                    postDto.setLiked(reactionService.hasUserLikedPost(post.getId(), user.getId()));
                    postDto.setSaved(hasUserSavedPost(post.getId(), user.getId()));
                    return postDto;
                }).collect(Collectors.toList());

        return new PageImpl<>(postDtos, pageable, posts.getTotalElements());
    }

    public Page<PostDto> getUserPosts(ApplicationUserDto user, Pageable pageable) {
        ApplicationUser loggedInUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser requestedUser = applicationUserRepository.findById(user.getId()).orElseThrow();
        if(loggedInUser == requestedUser) {
            //This will show all posts (including private) because they are looking at their own userProfile
            Page<Post> posts = postRepository.findAllByUser(requestedUser, pageable);
            return new PageImpl<>(posts.getContent().stream().map(PostDto::toDtoNoComments).collect(Collectors.toList()), pageable, posts.getTotalElements());
        }
        //currently, will just find public and friends posts. Need a utility to see if they are friends
        Page<Post> posts = postRepository.findByUser(requestedUser, pageable);
        return new PageImpl<>(posts.getContent().stream().map(PostDto::toDtoNoComments).collect(Collectors.toList()), pageable, posts.getTotalElements());
    }

    public PostDto createPost(PostDto post) {
        return PostDto.toDto(postRepository.save(post.toNewPostEntity()), List.of());
    }

    public Integer getCommentCountForPost(Long postId) {
        return postRepository.findById(postId).orElseThrow().getComments().size();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow();
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public boolean hasUserSavedPost(Long postId, Long userId) {
        return savedPostRepository.existsByPostIdAndUserId(postId, userId);
    }

    public SavedPostDto userSavePost(Long postId, Long userId) {
        SavedPost savedPost = new SavedPost();
        Post post = postRepository.findById(postId).orElseThrow();
        ApplicationUser user = applicationUserRepository.findById(userId).orElseThrow();
        savedPost.setPost(post);
        savedPost.setUser(user);
        savedPostRepository.save(savedPost);
        return SavedPostDto.toDto(savedPost);
    }

    public SavedPostDto userUnsavePost(Long postId, Long userId) {
        SavedPost savedPost = savedPostRepository.findByPostIdAndUserId(postId, userId).orElseThrow();
        savedPostRepository.delete(savedPost);
        return SavedPostDto.toDto(savedPost);
    }

}
