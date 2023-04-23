package com.staticvoid.songsuggestion.service;

import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.repository.SongRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    public Page<Song> search(String query, Pageable pageable) {
        return songRepository.search(query, pageable);
    }
}
