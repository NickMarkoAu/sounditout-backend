package com.staticvoid.songsuggestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.image.service.ImageRecognitionService;
import com.staticvoid.songsuggestion.repository.SongMetadataRepository;
import com.staticvoid.spotify.service.SpotifyService;
import com.staticvoid.image.recognition.service.RekognitionService;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.dto.SongDto;
import com.staticvoid.songsuggestion.repository.SongRepository;
import com.staticvoid.textgeneration.service.TextGenerationChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SongSuggestionServiceSingleShot implements SongSuggestionService {
    private final TextGenerationChatService textGenerationChatService;
    private final RekognitionService rekognitionService;
    private final SongRepository songRepository;
    private final SongMetadataRepository songMetadataRepository;
    private final SpotifyService spotifyService;

    @Override
    public final Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh) {
        String prompt = String.format("List 5 real songs in json format, that have been released publicly, that are associated with these tags: %s \n use this json format. Do not include anything in your response apart from JSON" + "\n [\n" + "{\n" + "\"name\": \"Empire State of Mind\",\n" + "\"artist\": \"Jay-Z ft. Alicia Keys\",\n" + "\"releasedYear\": \"2009\",\n" + "\"tags\": [\"city\", \"urban\", \"skyscraper\", \"metropolis\", \"downtown\"]\n" + "}]", tags.toString());
        if(refresh) {
            Song[] existingSongs = songRepository.findByImageId(imageId).toArray(new Song[0]);
            String additionalPrompt = String.format("%n Do not include any of these songs %s", toPromptString(existingSongs));
            prompt = prompt + additionalPrompt;
        }
        return getSongEntities(prompt, imageId);
    }

    @Override
    public final Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh, int energy, int tempo, int warmth) {
        String prompt = String.format("List 5 real songs in json format, that have been released publicly, that are associated with these tags: %s \n Use these values to choose the songs, these values are in a range of 1-10. Only select songs that are rated with the exact same values, energy: %s, tempo: %s, warmth: %s \n use this json format. Do not include anything in your response apart from JSON" + "\n [\n" + "{\n" + "\"name\": \"Empire State of Mind\",\n" + "\"artist\": \"Jay-Z ft. Alicia Keys\",\n" + "\"releasedYear\": \"2009\",\n" + "\"tags\": [\"city\", \"urban\", \"skyscraper\", \"metropolis\", \"downtown\"]\n" + "}]", tags.toString(), energy, tempo, warmth);
        if(refresh) {
            Song[] existingSongs = songRepository.findByImageId(imageId).toArray(new Song[0]);
            String additionalPrompt = String.format("%n Do not include any of these songs %s", toPromptString(existingSongs));
            prompt = prompt + additionalPrompt;
        }
        return getSongEntities(prompt, imageId);
    }

    @Override
    public final Song[] getSongEntities(String prompt, Long imageId) {
        SongSuggestionServiceSingleShot.log.info("Asking prompt: {}", prompt);

        String response = textGenerationChatService.generateFromPrompt(prompt);

        return getSongsFromResponse(response, imageId);
    }

    public ImageRecognitionService getImageRecognitionService() {
        return rekognitionService;
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
