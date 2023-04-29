package com.staticvoid.songsuggestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.domain.dto.ImageDto;
import com.staticvoid.image.recognition.service.RekognitionService;
import com.staticvoid.image.service.ImageRecognitionService;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.SongMetadata;
import com.staticvoid.songsuggestion.domain.dto.GenerateResultDto;
import com.staticvoid.songsuggestion.domain.dto.SongDto;
import com.staticvoid.songsuggestion.repository.SongMetadataRepository;
import com.staticvoid.songsuggestion.repository.SongRepository;
import com.staticvoid.spotify.service.SpotifyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface SongSuggestionService {
    Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh);

    Song[] songSuggestions(List<String> tags, Long imageId, Boolean refresh, int energy, int tempo, int warmth);

    default Song[] songSuggestions(List<String> tags) {
        return songSuggestions(tags, null, false);
    }

    Song[] getSongEntities(String prompt, Long imageId);

    default Song[] songSuggestions(Image image) {
        List<String> tags = getImageRecognitionService().detectImageLabels(image);
        return songSuggestions(tags, image.getId(), false);
    }

    default Song[] songSuggestions(Image image, int energy, int tempo, int warmth) {
        List<String> tags = getImageRecognitionService().detectImageLabels(image);
        return songSuggestions(tags, image.getId(), false, energy, tempo, warmth);
    }

    default GenerateResultDto songSuggestionResult(Image image) {
        List<String> tags = getImageRecognitionService().detectImageLabels(image);
        Song[] songs = songSuggestions(tags, image.getId(), false);
        SongDto[] songDtos = Arrays.stream(songs).map(SongDto::toDto).toArray(SongDto[]::new);
        return new GenerateResultDto(songDtos, tags.toArray(new String[0]), ImageDto.toDto(image));
    }

    default GenerateResultDto songSuggestionResult(Image image, int energy, int tempo, int warmth) {
        List<String> tags = getImageRecognitionService().detectImageLabels(image);
        Song[] songs = songSuggestions(tags, image.getId(), false, energy, tempo, warmth);
        SongDto[] songDtos = Arrays.stream(songs).map(SongDto::toDto).toArray(SongDto[]::new);
        return new GenerateResultDto(songDtos, tags.toArray(new String[0]), ImageDto.toDto(image));
    }

    default Song[] reloadSongSuggestions(Image image) {
        List<String> tags = Arrays.asList(ImageDto.toDto(image).getTags());
        return songSuggestions(tags, image.getId(), true);
    }

    default String toPromptString(Song[] song) {
        return Arrays.stream(song).map(Song::toPromptString).collect(Collectors.toList()).toString();
    }

    default SongMetadata getSongMetadata(Long songId) {
        Song song = getSongRepository().findById(songId).orElseThrow(() -> new RuntimeException("Song not found"));
        //Try to find song metadata in database, if not found get from Spotify
        return getSongMetadataRepository().findById(songId).orElse(getSpotifyService().getSongMetadata(song));
    }

    default Song[] getSongsFromResponse(String response, Long imageId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SongDto[] songs = mapper.readValue(response, SongDto[].class);
            List<Song> songList = new ArrayList<>();
            for (SongDto song : songs) {
                song.setImageId(imageId);
                Song songEntity = song.toEntity();
                getSongRepository().save(songEntity);
                songList.add(songEntity);
            }

            return songList.toArray(new Song[0]);
        } catch (Exception e) {
            throw new RuntimeException("Could not return suggestions", e);
        }
    }

    ImageRecognitionService getImageRecognitionService();

    SongRepository getSongRepository();

    SpotifyService getSpotifyService();

    SongMetadataRepository getSongMetadataRepository();
}
