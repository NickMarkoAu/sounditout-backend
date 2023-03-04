package com.staticvoid.text.service;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TextGenerationService {

    //TODO move this to config
    private static final String API_KEY = "";
    //TODO play with temperature, let client choose this within a range?
    private static final Double TEMPERATURE = 0.5;
    private static final String MODEL = "gpt-3.5-turbo";
    private static final Integer MAX_TOKENS = 500;
    private static final Duration TIMEOUT = Duration.of(60L, ChronoUnit.SECONDS);

    public String generateFromPrompt(String prompt) {
        OpenAiService service = new OpenAiService(API_KEY, TIMEOUT);

        List<ChatMessage> chatMessages = List.of(new ChatMessage("user",prompt));
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .model(MODEL)
                .maxTokens(MAX_TOKENS)
                .temperature(TEMPERATURE)
                .build();

        ChatCompletionResult completion = service.createChatCompletion(completionRequest);
        log.info(completion.getId());
        List<ChatCompletionChoice> choices = completion.getChoices();
        String result = choices.stream().map(choice -> choice.getMessage().getContent())
                .collect(Collectors.toList()).get(0)
                .replace("\n", "")
                .replace("\r", "");

        log.info(result);
        return result;
    }
}
