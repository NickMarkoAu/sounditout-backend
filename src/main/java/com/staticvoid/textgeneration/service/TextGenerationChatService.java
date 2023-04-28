package com.staticvoid.textgeneration.service;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TextGenerationChatService {

    private final String apiKey;
    private final String model;
    private static final Double TEMPERATURE = 0.5;
    private static final Integer MAX_TOKENS = 500;
    private static final Duration TIMEOUT = Duration.of(60L, ChronoUnit.SECONDS);

    public TextGenerationChatService(@Value("${openai.api-key}") String apiKey,
                                     @Value("${openai.model}") String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    public String generateFromPrompt(String prompt) {
        OpenAiService service = new OpenAiService(apiKey, TIMEOUT);

        List<ChatMessage> chatMessages = List.of(new ChatMessage("user",prompt));
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .model(model)
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
