package com.staticvoid.post.api;

import com.staticvoid.post.domain.Post;
import com.staticvoid.post.domain.PostDto;
import com.staticvoid.post.service.PostService;
import com.staticvoid.user.domain.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public Page<PostDto> getPostsForUser(@RequestParam("user") UserDto user, Pageable pageable) {
        return postService.getPostsForUser(user, pageable);
    }
}
