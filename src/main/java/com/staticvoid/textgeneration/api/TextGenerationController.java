package com.staticvoid.textgeneration.api;

import com.staticvoid.textgeneration.service.TextGenerationChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TextGenerationController {

    private final TextGenerationChatService service;

    @GetMapping("/prompt/{prompt}")
    public String generateTextFromPrompt(@PathVariable String prompt) {
        return service.generateFromPrompt(prompt);
    }
}