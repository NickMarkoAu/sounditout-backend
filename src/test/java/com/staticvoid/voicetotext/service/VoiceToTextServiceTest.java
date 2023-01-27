package com.staticvoid.voicetotext.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VoiceToTextServiceTest {

    private VoiceToTextService voiceToTextService;

    @BeforeEach
    void setup() {
        voiceToTextService = new VoiceToTextService();
    }

    @Test
    void should_transcribe_text_correctly() {
        String result = voiceToTextService.convertAudio("s3://staticvoid-openai-testing/audio/testing-testing-123.wav");
        String expected = "testing testing 123";

        assertEquals(expected, result);
    }
}