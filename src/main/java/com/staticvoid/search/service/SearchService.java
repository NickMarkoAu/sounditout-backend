package com.staticvoid.search.service;

import com.staticvoid.post.domain.Post;
import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.post.service.PostService;
import com.staticvoid.search.domain.Search;
import com.staticvoid.search.domain.dto.SearchDto;
import com.staticvoid.search.repository.SearchRepository;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.dto.SongDto;
import com.staticvoid.songsuggestion.service.SongService;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.domain.dto.ApplicationUserDto;
import com.staticvoid.user.service.ApplicationUserService;
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
public class SearchService {

    private SearchRepository searchRepository;
    private ApplicationUserService userService;
    private PostService postService;
    private SongService songService;

    public List<SearchDto> getRecentSearches(Long userId) {
        return searchRepository.findRecentSearchesByUserId(userId).stream().map(SearchDto::toDto).collect(Collectors.toList());
    }

    public Page<ApplicationUserDto> searchUsers(String query, Pageable pageable) {
        Page<ApplicationUser> userPage = userService.search(query, pageable);
        List<ApplicationUserDto> users = userPage.stream().map(ApplicationUserDto::toDtoNotSensitiveNotRecursive).collect(Collectors.toList());
        return new PageImpl<>(users, pageable, userPage.getTotalElements());
    }

    public Page<PostDto> searchPosts(String query, Pageable pageable) {
        Page<Post> postPage = postService.search(query, pageable);
        List<PostDto> posts = postPage.stream().map(PostDto::toDtoNoComments).collect(Collectors.toList());
        return new PageImpl<>(posts, pageable, postPage.getTotalElements());
    }

    public Page<SongDto> searchMusic(String query, Pageable pageable) {
        Page<Song> songPage = songService.search(query, pageable);
        List<SongDto> songs = songPage.stream().map(SongDto::toDto).collect(Collectors.toList());
        return new PageImpl<>(songs, pageable, songPage.getTotalElements());
    }

}
