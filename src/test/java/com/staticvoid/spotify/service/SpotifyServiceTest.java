package com.staticvoid.spotify.service;

import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.SongMetadata;
import com.staticvoid.songsuggestion.repository.SongMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class SpotifyServiceTest {

    private SpotifyService spotifyService;

    @Mock
    private SongMetadataRepository songMetadataRepository;

    @BeforeEach
    void setUp() {
        spotifyService = new SpotifyService(songMetadataRepository);
    }

    @Test
    void should_get_metadata() {
        spotifyService.init();
        Song song = new Song(1L, 1L, "New York, New York", "Frank Sinatra", "");
        SongMetadata expected = new SongMetadata();
        expected.setSong(song);
        when(songMetadataRepository.save(any())).thenReturn(expected);

        //Test
        SongMetadata metadata = spotifyService.getSongMetadata(song);

        log.info("Metadata: {}", metadata);
        assertThat(metadata).isEqualTo(expected);
    }

}