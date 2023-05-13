package com.staticvoid.textgeneration.service;

import com.staticvoid.textgeneration.util.MessageUtility;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private static final Duration TIMEOUT = Duration.of(60L, ChronoUnit.SECONDS);

    public TextGenerationChatService(@Value("${spring.open-ai.api-key}") String apiKey,
                                     @Value("${spring.open-ai.model}") String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    public String generateFromPrompt(String prompt) {
        OpenAiService service = new OpenAiService(apiKey, TIMEOUT);

        List<ChatMessage> chatMessages = List.of(new ChatMessage("user",prompt));
        return getResponse(service, chatMessages);
    }

    public List<ChatMessage> generateFromPromptMessages(String messageType) {
        List<ChatMessage> messages = MessageUtility.getMessagesForType(messageType);
        String initialPrompt = MessageUtility.getInitialPromptForType(messageType);
        return messages.stream().map(message -> {
            message.setContent(message.getContent()
                    .replace("#initialprompt", initialPrompt));
            return message;
        }).collect(Collectors.toList());
    }

    public String getResponse(List<ChatMessage> messages) {
        OpenAiService service = new OpenAiService(apiKey, TIMEOUT);
        return getResponse(service, messages);
    }

    @NotNull
    private String getResponse(OpenAiService service, List<ChatMessage> chatMessages) {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .model(model)
                .temperature(TEMPERATURE)
                .build();

        ChatCompletionResult completion = service.createChatCompletion(completionRequest);
        log.info("Prompt Tokens: {}", completion.getUsage().getPromptTokens());
        log.info("Completion Tokens: {}", completion.getUsage().getCompletionTokens());
        List<ChatCompletionChoice> choices = completion.getChoices();
        String result = choices.stream().map(choice -> choice.getMessage().getContent())
                .collect(Collectors.toList()).get(0)
                .replace("\n", "")
                .replace("\r", "");

        log.info(result);
        return result;
    }
}
