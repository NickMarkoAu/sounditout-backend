package com.spotify.service;

import com.staticvoid.songsuggestion.domain.Song;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.UUID;

@Slf4j
class SpotifyServiceTest {

    private SpotifyService spotifyService;

    @BeforeEach
    void setup() {
        spotifyService = new SpotifyService();
    }

    @Test
    void should_get_uri() {
        Song song = new Song(1L, UUID.randomUUID().toString(), "New York, New York", "Frank Sinatra", "", null);
        String url = spotifyService.getTrack(song);

        log.info("URL: {}", url);
    }

    @Test
    void should_get_list_of_uri() {

    }

}