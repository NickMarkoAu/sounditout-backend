package com.youtube.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
class YoutubeServiceTest {

    private final YoutubeService youtubeService = new YoutubeService();

    @Test
    void should_return_response() {
        try {
            List<SearchResult> result = youtubeService.getResponse("test");
            log.info(result.toString());
            assertFalse(result.isEmpty());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Could not query youtube", e);
        }
    }

}