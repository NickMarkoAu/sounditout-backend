package com.staticvoid.search.service;

import com.staticvoid.post.domain.Post;
import com.staticvoid.post.domain.dto.PostDto;
import com.staticvoid.post.service.PostService;
import com.staticvoid.profile.domain.UserProfile;
import com.staticvoid.profile.domain.dto.UserProfileDto;
import com.staticvoid.profile.service.UserProfileService;
import com.staticvoid.search.domain.Search;
import com.staticvoid.search.domain.dto.SearchDto;
import com.staticvoid.search.repository.SearchRepository;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.dto.SongDto;
import com.staticvoid.songsuggestion.service.SongService;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SearchService {

    private SearchRepository searchRepository;
    private UserProfileService userProfileService;
    private PostService postService;
    private SongService songService;

    public List<SearchDto> getRecentSearches(Long userId) {
        return searchRepository.findRecentSearchesByUserId(userId).stream().map(SearchDto::toDto).collect(Collectors.toList());
    }

    public Page<UserProfileDto> searchUsers(String query, Pageable pageable) {
        Page<UserProfile> userPage = userProfileService.search(query, pageable);
        List<UserProfileDto> users = userPage.stream().map(UserProfileDto::toDto).collect(Collectors.toList());
        saveSearch(query);
        return new PageImpl<>(users, pageable, userPage.getTotalElements());
    }

    public Page<PostDto> searchPosts(String query, Pageable pageable) {
        Page<Post> postPage = postService.search(query, pageable);
        List<PostDto> posts = postPage.stream().map(PostDto::toDtoNoComments).collect(Collectors.toList());
        saveSearch(query);
        return new PageImpl<>(posts, pageable, postPage.getTotalElements());
    }

    public Page<SongDto> searchMusic(String query, Pageable pageable) {
        Page<Song> songPage = songService.search(query, pageable);
        List<SongDto> songs = songPage.stream().map(SongDto::toDto).collect(Collectors.toList());
        saveSearch(query);
        return new PageImpl<>(songs, pageable, songPage.getTotalElements());
    }

    private void saveSearch(String query) {
        ApplicationUser loggedInUser = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Search search = new Search();
        search.setQuery(query);
        search.setUser(loggedInUser);
        search.setDate(new Date());
        searchRepository.save(search);
    }

}
