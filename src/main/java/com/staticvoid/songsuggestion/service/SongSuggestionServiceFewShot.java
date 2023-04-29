package com.staticvoid.songsuggestion.service;

import com.staticvoid.image.caption.domain.dto.CaptionResponseDto;
import com.staticvoid.image.caption.domain.dto.TagValueDto;
import com.staticvoid.image.caption.service.CaptionService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.domain.dto.ImageDto;
import com.staticvoid.image.recognition.service.RekognitionService;
import com.staticvoid.image.service.ImageRecognitionService;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.dto.GenerateResultDto;
import com.staticvoid.songsuggestion.domain.dto.SongDto;
import com.staticvoid.songsuggestion.repository.SongMetadataRepository;
import com.staticvoid.songsuggestion.repository.SongRepository;
import com.staticvoid.spotify.service.SpotifyService;
import com.staticvoid.textgeneration.service.TextGenerationChatService;
import com.staticvoid.textgeneration.util.MessageUtility;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SongSuggestionServiceFewShot implements SongSuggestionService {

    private final TextGenerationChatService textGenerationChatService;
    private final CaptionService recognitionService;
    private final SongRepository songRepository;
    private final SongMetadataRepository songMetadataRepository;
    private final SpotifyService spotifyService;

    @Override
    public Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh) {
        return new Song[0];
    }

    public Song[] songSuggestionsWithCaption(String caption, List<String> tags, Long imageId, Boolean refresh) {
        String prompt = String.format(MessageUtility.getRequestPromptForType("suggestions"), caption, tags.toString());
        //TODO implement refresh
        return getSongEntities(prompt, imageId);
    }

    @Override
    public GenerateResultDto songSuggestionResult(Image image) {
        CaptionResponseDto captionResponse = recognitionService.generateCaption(image);
        List<String> tags = Arrays.stream(captionResponse.getTagsResult().getValues()).map(TagValueDto::getName).collect(Collectors.toList());
        Song[] songs = songSuggestionsWithCaption(captionResponse.getCaptionResult().getText(), tags, image.getId(), false);
        SongDto[] songDtos = Arrays.stream(songs).map(SongDto::toDto).toArray(SongDto[]::new);
        return new GenerateResultDto(songDtos, tags.toArray(new String[0]), ImageDto.toDto(image));
    }

    @Override
    public Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh, int energy, int tempo, int warmth) {
        return new Song[0];
    }

    @Override
    public Song[] getSongEntities(String prompt, Long imageId) {
        List<ChatMessage> messages = textGenerationChatService.generateFromPromptMessages("suggestions");
        ChatMessage promptMessage = new ChatMessage("user", prompt);
        messages.add(promptMessage);

        String response = textGenerationChatService.getResponse(messages);

        return getSongsFromResponse(response, imageId);
    }

    @Override
    public ImageRecognitionService getImageRecognitionService() {
        return recognitionService;
    }

    @Override
    public SongRepository getSongRepository() {
        return songRepository;
    }

    @Override
    public SpotifyService getSpotifyService() {
        return spotifyService;
    }

    @Override
    public SongMetadataRepository getSongMetadataRepository() {
        return songMetadataRepository;
    }
}
