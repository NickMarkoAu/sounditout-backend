package com.staticvoid.post.reaction.api;

import com.staticvoid.post.reaction.domain.dto.ReactionDto;
import com.staticvoid.post.reaction.service.ReactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/api/reaction/like")
    public ResponseEntity<?> reactToPost(@RequestBody ReactionDto reaction) {
        try {
            if(reaction.getPost().isLiked()) {
                ReactionDto reactionResponse = reactionService.unreactToPost(reaction);
                return ResponseEntity.ok(reactionResponse);
            } else {
                if(!reactionService.hasUserLikedPost(reaction.getUser().getId(), reaction.getPost().getId())) {
                    ReactionDto reactionResponse = reactionService.reactToPost(reaction);
                    return ResponseEntity.ok(reactionResponse);
                }
            }
            return ResponseEntity.ok(reaction);
        } catch (Exception e) {
            log.error("Error reacting to post", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
