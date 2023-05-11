package com.staticvoid.spotify.service;

import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.SongMetadata;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class SpotifyServiceTest {

    @Autowired
    private SpotifyService spotifyService;

    @Test
    void should_get_metadata() {
        spotifyService.init();
        Song song = new Song(1L, 1L, "New York, New York", "Frank Sinatra", "");
        SongMetadata metadata = spotifyService.getSongMetadata(song);

        log.info("Metadata: {}", metadata);
    }

}