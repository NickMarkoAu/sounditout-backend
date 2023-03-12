package com.staticvoid.songsuggestion.api;

import com.staticvoid.fileupload.service.S3StorageService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.repository.ImageRepository;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.service.SongSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class SongSuggestionController {

    private final SongSuggestionService songSuggestionService;
    private final S3StorageService storageService = new S3StorageService();
    private final ImageRepository imageRepository;

    @GetMapping("/api/songs/")
    public Song[] suggestSongsFromImage(@RequestParam("file") MultipartFile file) {
        Image convFile = storageService.store(file);
        return songSuggestionService.songSuggestions(convFile);
    }

    @GetMapping("/api/reload-songs/")
    public Song[] reloadSongSuggestions(@RequestParam("imageId") Long imageId) {
        Image image = imageRepository.getReferenceById(imageId);
        return songSuggestionService.reloadSongSuggestions(image);
    }
}
