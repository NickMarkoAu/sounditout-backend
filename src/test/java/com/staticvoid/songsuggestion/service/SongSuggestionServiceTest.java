package com.staticvoid.songsuggestion.service;

import com.staticvoid.fileupload.service.S3StorageService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class SongSuggestionServiceTest {

    @Autowired
    private SongSuggestionService songSuggestionService;

    private final S3StorageService storageService = new S3StorageService();

    @Test
    void should_return_song_suggestions_from_tags() {
        List<String> tags = List.of("Urban", "Building", "Architecture", "Metropolis", "Office Building", "Waterfront", "Water", "High Rise", "Boat", "Vehicle", "Transportation", "Tower", "Sky", "Outdoors", "Nature", "Downtown", "Scenery", "Skyscraper");

        Song[] songs = songSuggestionService.songSuggestions(tags);

        log.info("Songs: {}", Arrays.toString(songs));

        assertEquals(songs.length, 5);
    }

    @Test
    void should_return_song_suggestions_from_image() throws FileNotFoundException {
        File file = Paths.get("src", "test", "resources", "img", "test-image.jpg").toFile();
        ApplicationUser user = new ApplicationUser();
        user.setId(12345L);
        Image image = storageService.putImage(user, "test-image.jpg", file);

        Song[] songs = songSuggestionService.songSuggestions(image);

        log.info("Songs: {}", Arrays.toString(songs));

        assertEquals(songs.length, 5);
    }

    @Test
    void should_return_song_suggestions_from_image2() throws FileNotFoundException {
        File file = Paths.get("src", "test", "resources", "img", "test-image2.jpg").toFile();
        ApplicationUser user = new ApplicationUser();
        user.setId(12345L);
        Image image = storageService.putImage(user, "test-image2.jpg", file);
        Song[] songs = songSuggestionService.songSuggestions(image);

        log.info("Songs: {}", Arrays.toString(songs));

        assertEquals(songs.length, 5);
    }

    @Test
    void should_return_song_suggestions_from_image3() throws FileNotFoundException {
        File file = Paths.get("src", "test", "resources", "img", "test-image3.jpg").toFile();
        ApplicationUser user = new ApplicationUser();
        user.setId(12345L);
        Image image = storageService.putImage(user, "test-image3.jpg", file);
        Song[] songs = songSuggestionService.songSuggestions(image);

        log.info("Songs: {}", Arrays.toString(songs));

        assertEquals(songs.length, 5);
    }

    @Test
    void should_return_song_suggestions_from_image4() throws FileNotFoundException {
        File file = Paths.get("src", "test", "resources", "img", "test-image4.jpg").toFile();
        ApplicationUser user = new ApplicationUser();
        user.setId(12345L);
        Image image = storageService.putImage(user, "test-image4.jpg", file);
        Song[] songs = songSuggestionService.songSuggestions(image);

        log.info("Songs: {}", Arrays.toString(songs));

        assertEquals(songs.length, 5);
    }

    @Test
    void should_return_song_suggestions_from_image5() throws FileNotFoundException {
        File file = Paths.get("src", "test", "resources", "img", "test-image5.jpg").toFile();
        ApplicationUser user = new ApplicationUser();
        user.setId(12345L);
        Image image = storageService.putImage(user, "test-image5.jpg", file);

        Song[] songs = songSuggestionService.songSuggestions(image);

        log.info("Songs: {}", Arrays.toString(songs));

        assertEquals(songs.length, 5);
    }

    @Test
    void should_return_song_suggestions_from_image_with_customisation_values() throws FileNotFoundException {
        File file = Paths.get("src", "test", "resources", "img", "test-image.jpg").toFile();
        ApplicationUser user = new ApplicationUser();
        user.setId(12345L);
        Image image = storageService.putImage(user, "test-image.jpg", file);

        int energy = 5;
        int tempo = 5;
        int warmth = 2;

        Song[] songs = songSuggestionService.songSuggestions(image, energy, tempo, warmth);

        log.info("Songs: {}", Arrays.toString(songs));

        assertEquals(songs.length, 5);
    }

    @Test
    void should_select_different_songs_with_different_values() throws FileNotFoundException {
        File file = Paths.get("src", "test", "resources", "img", "test-image.jpg").toFile();
        ApplicationUser user = new ApplicationUser();
        user.setId(12345L);
        Image image = storageService.putImage(user, "test-image.jpg", file);

        int energy = 5;
        int tempo = 5;
        int warmth = 2;

        Song[] songs1 = songSuggestionService.songSuggestions(image, energy, tempo, warmth);

        log.info("Songs #1: {}", Arrays.toString(songs1));

        energy = 9;
        tempo = 9;
        warmth = 9;

        Song[] songs2 = songSuggestionService.songSuggestions(image, energy, tempo, warmth);

        Arrays.asList(songs1).forEach(song -> {
            Arrays.asList(songs2).forEach(song2 -> {
                assertNotEquals(song, song2);
            });
        });
    }


}