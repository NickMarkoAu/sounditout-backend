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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void should_return_song_suggestions_from_image() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image.jpg");
        try {
            File file = new File(dir_url.toURI());
            ApplicationUser user = new ApplicationUser();
            user.setId(12345L);
            Image image = storageService.putImage(user, "test-image.jpg", file);

            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image2() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image2.jpg");
        try {
            File file = new File(dir_url.toURI());
            ApplicationUser user = new ApplicationUser();
            user.setId(12345L);
            Image image = storageService.putImage(user, "test-image2.jpg", file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image3() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image3.png");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    @Disabled
    void should_return_song_suggestions_from_image4() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image4.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image5() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image5.jpg");
        try {
            File file = new File(dir_url.toURI());
            ApplicationUser user = new ApplicationUser();
            user.setId(12345L);
            Image image = storageService.putImage(user, "test-image5.jpg", file);

            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    @Disabled
    void should_return_song_suggestions_from_image6() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image6.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    @Disabled
    void should_return_song_suggestions_from_image7() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image7.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image8() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image8.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }


    @Test
    void should_return_song_suggestions_with_genres() {
        URL dir_url = ClassLoader.getSystemResource("img/test-image.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            List<String> genres = List.of("Rock", "Hip hop", "Country");
            Song[] songs = songSuggestionService.songSuggestionsWithGenres(image, genres);
            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image_() {
        URL dir_url = ClassLoader.getSystemResource("img/test-images/test-image.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image2_() {
        URL dir_url = ClassLoader.getSystemResource("img/test-images/test-image2.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image3_() {
        URL dir_url = ClassLoader.getSystemResource("img/test-images/test-image3.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image4_() {
        URL dir_url = ClassLoader.getSystemResource("img/test-images/test-image4.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image5_() {
        URL dir_url = ClassLoader.getSystemResource("img/test-images/test-image5.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

    @Test
    void should_return_song_suggestions_from_image6_() {
        URL dir_url = ClassLoader.getSystemResource("img/test-images/test-image6.jpg");
        try {
            File file = new File(dir_url.toURI());
            Image image = new Image();
            image.setFile(file);
            Song[] songs = songSuggestionService.songSuggestions(image);

            log.info("Songs: {}", Arrays.toString(songs));

            assertEquals(songs.length, 5);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not convert image to suggestions");
        }
    }

}