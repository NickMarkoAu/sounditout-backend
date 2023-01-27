package com.staticvoid.text.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Slf4j
class TextGenerationServiceTest {

    private TextGenerationService service;

    @BeforeEach
    void setup() {
        service = new TextGenerationService();
    }

    @Test
    void should_generate_text() {
        String prompt = "Generate 1 word of text";
        String result = service.generateFromPrompt(prompt);

        log.info(result);
        assertEquals(result.split(" ").length, 1);
    }

}