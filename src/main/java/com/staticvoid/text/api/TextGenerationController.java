package com.staticvoid.text.api;

import com.staticvoid.text.service.TextGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TextGenerationController {

    private final TextGenerationService service;

    @GetMapping("/prompt/{prompt}")
    public String generateTextFromPrompt(@PathVariable String prompt) {
        return service.generateFromPrompt(prompt);
    }
}
