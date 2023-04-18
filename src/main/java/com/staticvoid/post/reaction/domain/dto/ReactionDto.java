package com.staticvoid.post.reaction.domain.dto;

import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.post.reaction.domain.Reaction;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ReactionDto {
    private Long id;
    private ApplicationUserDto user;
    private PostDto post;
    private ReactionType reactionType;

    public Reaction toEntity() {
        Reaction reaction = new Reaction();
        reaction.setId(id);
        reaction.setUser(user.toEntityNotRecursive());
        try{
            reaction.setPost(post.toEntityNoComments());
        } catch(Exception e) {
            log.error("Could not set post for reaction", e);
        }
        reaction.setReactionType(reactionType);
        return reaction;
    }

    public static ReactionDto toDto(Reaction entity) {
        ReactionDto reactionDto = new ReactionDto();
        reactionDto.setId(entity.getId());
        reactionDto.setUser(ApplicationUserDto.toDtoNotRecursive(entity.getUser()));
        reactionDto.setPost(PostDto.toDtoNoComments(entity.getPost()));
        reactionDto.setReactionType(entity.getReactionType());
        return reactionDto;
    }

    public enum ReactionType {
        LIKE,
        LOVE,
        HAHA,
        WOW,
        SAD,
        ANGRY
    }
}
