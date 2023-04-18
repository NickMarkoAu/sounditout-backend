package com.staticvoid.post.reaction.service;

import com.staticvoid.post.domain.Post;
import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.post.reaction.domain.Reaction;
import com.staticvoid.post.reaction.domain.dto.ReactionDto;
import com.staticvoid.post.reaction.respository.ReactionRepository;
import com.staticvoid.post.repository.PostRepository;
import com.staticvoid.post.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;

    public ReactionDto reactToPost(ReactionDto reaction) {
        Post post = postRepository.findById(reaction.getPost().getId()).orElseThrow();
        long postLikes = post.getLikes() != null ? post.getLikes() : 0;
        postLikes++;
        post.setLikes(postLikes);
        postRepository.save(post);
        reactionRepository.save(reaction.toEntity());
        return reaction;
    }

    public ReactionDto unreactToPost(ReactionDto reaction) {
        Post post = postRepository.findById(reaction.getPost().getId()).orElseThrow();
        long postLikes = post.getLikes() != null ? post.getLikes() : 0;
        postLikes--;
        post.setLikes(postLikes);
        Reaction reactionEntity = reactionRepository.findByPostIdAndUserId(reaction.getPost().getId(), reaction.getUser().getId()).orElseThrow();
        postRepository.save(post);
        reactionRepository.delete(reactionEntity);
        return ReactionDto.toDto(reactionEntity);
    }

    public boolean hasUserLikedPost(Long postId, Long userId) {
        return reactionRepository.existsByPostIdAndUserId(postId, userId);
    }
}
