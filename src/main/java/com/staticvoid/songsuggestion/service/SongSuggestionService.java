package com.staticvoid.songsuggestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.songsuggestion.domain.GenerateResultDto;
import com.staticvoid.spotify.service.SpotifyService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.domain.ImageDto;
import com.staticvoid.image.recognition.service.ImageRecognitionService;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.SongDto;
import com.staticvoid.songsuggestion.repository.SongRepository;
import com.staticvoid.text.service.TextGenerationChatService;
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
    private final SpotifyService spotifyService;

    public final Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh) {
        //TODO get these prompts from resource files
        //TODO try removing bias by taking out song suggestion
        String prompt = String.format("List 5 real songs in json format, that have been released publicly, that are associated with these tags: %s \n use this json format" + "\n [\n" + "{\n" + "\"name\": \"Empire State of Mind\",\n" + "\"artist\": \"Jay-Z ft. Alicia Keys\",\n" + "\"releasedYear\": \"2009\",\n" + "\"tags\": [\"city\", \"urban\", \"skyscraper\", \"metropolis\", \"downtown\"]\n" + "}]", tags.toString());
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

    public final Song[] songSuggestionsWithGenres(List<String> tags, Long imageId, List<String> genres) {
        String prompt = String.format("List 5 real songs in json format that are associated with these tags: %s" + "\n in these genres: %s" + " \n use this json format" + "\n [\n" + "{\n" + "\"name\": \"Empire State of Mind\",\n" + "\"artist\": \"Jay-Z ft. Alicia Keys\",\n" + "\"tags\": [\"city\", \"urban\", \"skyscraper\", \"metropolis\", \"downtown\"]\n" + "}]", tags.toString(), genres.toString());

        return getSongEntities(prompt, imageId);
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
//                addSpotifyPreview(songEntity);
                songList.add(songEntity);
            }

            return songList.toArray(new Song[0]);
        } catch (Exception e) {
            throw new RuntimeException("Could not return suggestions", e);
        }
    }

    private void addSpotifyPreview(Song song) {
        song.setPreviewUrl(spotifyService.getTrack(song));
    }

    public final Song[] songSuggestions(Image image) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        return songSuggestions(tags, image.getId(), false);
    }

    public final GenerateResultDto songSuggestionResult(Image image) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        Song[] songs = songSuggestions(tags, image.getId(), false);
        SongDto[] songDtos = Arrays.stream(songs).map(SongDto::toDto).toArray(SongDto[]::new);
        return new GenerateResultDto(songDtos, tags.toArray(new String[0]), ImageDto.toDto(image));
    }

    public final Song[] songSuggestionsWithGenres(Image image, List<String> genres) {
        List<String> tags = imageRecognitionService.detectImageLabels(image);
        return songSuggestionsWithGenres(tags, image.getId(), genres);
    }

    public final Song[] reloadSongSuggestions(Image image) {
        List<String>tags = Arrays.asList(ImageDto.toDto(image).getTags());
        return songSuggestions(tags, image.getId(), true);
    }

    private String toPromptString(Song[] song) {
        return Arrays.stream(song).map(Song::toPromptString).collect(Collectors.toList()).toString();
    }


}
