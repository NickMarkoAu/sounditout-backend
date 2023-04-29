package com.staticvoid.textgeneration.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class MessageUtility {

    public static List<ChatMessage> getMessagesForType(String messageType) {
        List<ChatMessage> messages;
        log.info("Getting messages for message type: {}", messageType);
        try(InputStream in=Thread.currentThread().getContextClassLoader().getResourceAsStream("prompts/training/" + messageType + ".json")){
            ObjectMapper mapper = new ObjectMapper();
            messages = mapper.readValue(in, new TypeReference<List<ChatMessage>>(){});
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        return messages;
    }

    public static String getInitialPromptForType(String messageType) {
        try {
            URL url = Resources.getResource("prompts/templates/" + messageType + ".txt");
            return Resources.toString(url, StandardCharsets.UTF_8);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String getRequestPromptForType(String messageType) {
        try {
            URL url = Resources.getResource("prompts/request/" + messageType + ".txt");
            return Resources.toString(url, StandardCharsets.UTF_8);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
