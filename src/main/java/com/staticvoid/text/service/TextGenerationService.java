package com.staticvoid.text.service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TextGenerationService {

    //TODO move this to config
    private static final String API_KEY = "";
    //TODO play with temperature, let client choose this within a range?
    private static final Double TEMPERATURE = 0.5;
    private static final String MODEL = "text-davinci-003";
    private static final Integer MAX_TOKENS = 500;
    private static final Duration TIMEOUT = Duration.of(60L, ChronoUnit.SECONDS);

    public String generateFromPrompt(String prompt) {
        OpenAiService service = new OpenAiService(API_KEY, TIMEOUT);

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model(MODEL)
                .maxTokens(MAX_TOKENS)
                .temperature(TEMPERATURE)
                .echo(false)
                .build();

        CompletionResult completion = service.createCompletion(completionRequest);
        log.info(completion.getId());
        List<CompletionChoice> choices = completion.getChoices();
        String result = choices.stream().map(CompletionChoice::getText)
                .collect(Collectors.toList()).get(0)
                .replace("\n", "")
                .replace("\r", "");

        log.info(result);
        return result;
    }
}
