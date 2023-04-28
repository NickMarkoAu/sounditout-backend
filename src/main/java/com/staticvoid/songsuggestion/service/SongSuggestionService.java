package com.staticvoid.songsuggestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.songsuggestion.domain.SongMetadata;
import com.staticvoid.songsuggestion.domain.dto.GenerateResultDto;
import com.staticvoid.songsuggestion.repository.SongMetadataRepository;
import com.staticvoid.spotify.service.SpotifyService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.domain.dto.ImageDto;
import com.staticvoid.image.recognition.service.ImageRecognitionService;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.dto.SongDto;
import com.staticvoid.songsuggestion.repository.SongRepository;
import com.staticvoid.textgeneration.service.TextGenerationChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SongSuggestionService {
    private final TextGenerationChatService textGenerationChatService;
    private final ImageRecognitionService imageRecognitionService;
    private final SongRepository songRepository;
    private final SongMetadataRepository songMetadataRepository;
    private final SpotifyService spotifyService;

    public final Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh) {
        //TODO get these prompts from resource files
        //TODO try removing bias by taking out song suggestion
        String prompt = String.format("List 5 real songs in json format, that have been released publicly, that are associated with these tags: %s \n use this json format. Do not include anything in your response apart from JSON" + "\n [\n" + "{\n" + "\"name\": \"Empire State of Mind\",\n" + "\"artist\": \"Jay-Z ft. Alicia Keys\",\n" + "\"releasedYear\": \"2009\",\n" + "\"tags\": [\"city\", \"urban\", \"skyscraper\", \"metropolis\", \"downtown\"]\n" + "}]", tags.toString());
        if(refresh) {
            Song[] existingSongs = songRepository.findByImageId(imageId).toArray(new Song[0]);
            String additionalPrompt = String.format("\n Do not include any of these songs %s", toPromptString(existingSongs));
            prompt = prompt + additionalPrompt;
        }
        return getSongEntities(prompt, imageId);
    }

    public final Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh, int energy, int tempo, int warmth) {
        String prompt = String.format("List 5 real songs in json format, that have been released publicly, that are associated with these tags: %s \n Use these values to choose the songs, these values are in a range of 1-10. Only select songs that are rated with the exact same values, energy: %s, tempo: %s, warmth: %s \n use this json format. Do not include anything in your response apart from JSON" + "\n [\n" + "{\n" + "\"name\": \"Empire State of Mind\",\n" + "\"artist\": \"Jay-Z ft. Alicia Keys\",\n" + "\"releasedYear\": \"2009\",\n" + "\"tags\": [\"city\", \"urban\", \"skyscraper\", \"metropolis\", \"downtown\"]\n" + "}]", tags.toString(), energy, tempo, warmth);
        if(refresh) {
            Song[] existingSongs = songRepository.findByImageId(imageId).toArray(new Song[0]);
            String additionalPrompt = String.format("\n Do not include any of these songs %s", toPromptString(existingSongs));
            prompt = prompt + additionalPrompt;
        }
        return getSongEntities(prompt, imageId);
    }

    public final Song[] songSuggestions(List<String> tags) {
        return songSuggestions(tags, null, false);
    }

    private Song[] getSongEntities(String prompt, Long imageId) {
        log.info("Asking prompt: {}", prompt);

        String response = textGenerationChatService.generateFromPrompt(prompt);

        try {
            ObjectMapper mapper = new ObjectMapper();
            SongDto[] songs = mapper.readValue(response, SongDto[].class);
            List<Song> songList = new ArrayList<>();
            for (SongDto song : songs) {
                song.setImageId(imageId);
                Song songEntity = song.toEntity();
                songRepository.save(songEntity);
                songList.add(songEntity);
            }

            return songList.toArray(new Song[0]);
        } catch (Exception e) {
            throw new RuntimeException("Could not return suggestions", e);
        }
    }

    public final Song[] songSuggestions(Image image) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        return songSuggestions(tags, image.getId(), false);
    }

    public final Song[] songSuggestions(Image image, int energy, int tempo, int warmth) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        return songSuggestions(tags, image.getId(), false, energy, tempo, warmth);
    }

    public final GenerateResultDto songSuggestionResult(Image image) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        Song[] songs = songSuggestions(tags, image.getId(), false);
        SongDto[] songDtos = Arrays.stream(songs).map(SongDto::toDto).toArray(SongDto[]::new);
        return new GenerateResultDto(songDtos, tags.toArray(new String[0]), ImageDto.toDto(image));
    }

    public final GenerateResultDto songSuggestionResult(Image image, int energy, int tempo, int warmth) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        Song[] songs = songSuggestions(tags, image.getId(), false, energy, tempo, warmth);
        SongDto[] songDtos = Arrays.stream(songs).map(SongDto::toDto).toArray(SongDto[]::new);
        return new GenerateResultDto(songDtos, tags.toArray(new String[0]), ImageDto.toDto(image));
    }

    public final Song[] reloadSongSuggestions(Image image) {
        List<String>tags = Arrays.asList(ImageDto.toDto(image).getTags());
        return songSuggestions(tags, image.getId(), true);
    }

    private String toPromptString(Song[] song) {
        return Arrays.stream(song).map(Song::toPromptString).collect(Collectors.toList()).toString();
    }

    public SongMetadata getSongMetadata(Long songId) {
        Song song = songRepository.findById(songId).orElseThrow(() -> new RuntimeException("Song not found"));
        //Try to find song metadata in database, if not found get from Spotify
        return songMetadataRepository.findById(songId).orElse(spotifyService.getSongMetadata(song));
    }
}
