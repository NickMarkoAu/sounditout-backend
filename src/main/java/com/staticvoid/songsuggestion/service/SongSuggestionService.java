package com.staticvoid.songsuggestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.recognition.service.ImageRecognitionService;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.SongDto;
import com.staticvoid.songsuggestion.repository.SongRepository;
import com.staticvoid.text.service.TextGenerationService;
import com.youtube.service.YoutubeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SongSuggestionService {
    private final TextGenerationService textGenerationService;
    private final ImageRecognitionService imageRecognitionService;
    private final YoutubeService youtubeService;
    @Autowired
    private SongRepository songRepository;

    public SongSuggestionService() {
        this.textGenerationService = new TextGenerationService();
        this.imageRecognitionService = new ImageRecognitionService();
        this.youtubeService = new YoutubeService();
    }

    public final Song[] songSuggestions(List<String> tags, String imageId) {
        //TODO get these prompts from resource files
        //TODO try removing bias by taking out song suggestion
        //TODO work on prompt design so it doesn't return any fake songs
        String prompt = String.format("List 5 real songs in json format that are associated with these tags: %s \n use this json format" + "\n [\n" + "{\n" + "\"name\": \"Empire State of Mind\",\n" + "\"artist\": \"Jay-Z ft. Alicia Keys\",\n" + "\"tags\": [\"city\", \"urban\", \"skyscraper\", \"metropolis\", \"downtown\"]\n" + "}]", tags.toString());

        return getSongEntities(prompt, imageId);
    }

    public final Song[] songSuggestions(List<String> tags) {
        return songSuggestions(tags, null);
    }

    public final Song[] songSuggestions(List<String> tags, String imageId, List<String> genres) {
        String prompt = String.format("List 5 real songs in json format that are associated with these tags: %s" + "\n in these genres: %s" + " \n use this json format" + "\n [\n" + "{\n" + "\"name\": \"Empire State of Mind\",\n" + "\"artist\": \"Jay-Z ft. Alicia Keys\",\n" + "\"tags\": [\"city\", \"urban\", \"skyscraper\", \"metropolis\", \"downtown\"]\n" + "}]", tags.toString(), genres.toString());

        return getSongEntities(prompt, imageId);
    }

    private Song[] getSongEntities(String prompt, String imageId) {
        log.info("Asking prompt: {}", prompt);

        String response = textGenerationService.generateFromPrompt(prompt);

        try {
            ObjectMapper mapper = new ObjectMapper();
            SongDto[] songs = mapper.readValue(response, SongDto[].class);
            List<Song> songList = new ArrayList<>();
            for (SongDto song : songs) {
                this.addVideoId(song);
                song.setImageId(imageId);
                Song songEntity = song.toEntity();
//                songRepository.save(songEntity);
                songList.add(songEntity);
            }

            return songList.toArray(new Song[0]);
        } catch (Exception e) {
            throw new RuntimeException("Could not return suggestions", e);
        }
    }

    private void addVideoId(SongDto song) {
        try {
            song.setYoutubeVideoId(youtubeService.getResponse(song.getName() + " " + song.getArtist()).get(0).getId().getVideoId());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Could not add video id", e);
        }
    }

    public final Song[] songSuggestions(Image image) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        return songSuggestions(tags, image.getId());
    }

    public final Song[] songSuggestions(Image image, List<String> genres) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        return songSuggestions(tags, image.getId(), genres);
    }


}
