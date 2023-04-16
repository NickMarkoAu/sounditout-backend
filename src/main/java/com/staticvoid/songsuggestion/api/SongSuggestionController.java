package com.staticvoid.songsuggestion.api;

import com.staticvoid.fileupload.service.S3StorageService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.repository.ImageRepository;
import com.staticvoid.songsuggestion.domain.SongMetadata;
import com.staticvoid.songsuggestion.domain.dto.GenerateResultDto;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.service.SongSuggestionService;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.user.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SongSuggestionController {

    private final SongSuggestionService songSuggestionService;
    private final S3StorageService storageService = new S3StorageService();
    private final ImageRepository imageRepository;
    private final ApplicationUserService applicationUserService;

    @PostMapping("/api/songs/")
    public ResponseEntity<GenerateResultDto> suggestSongsFromImage(@RequestParam("file") MultipartFile file) {
        try {
            ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Image convFile = storageService.store(file, user);
            imageRepository.save(convFile);
            GenerateResultDto generateResultDto = songSuggestionService.songSuggestionResult(convFile);
            return ResponseEntity.ok(generateResultDto);
        } catch(Exception e) {
            log.error("Could not return song suggestions: ", e);
            return ResponseEntity.internalServerError().body(new GenerateResultDto(e.getMessage()));
        }
    }

    @GetMapping("/api/reload-songs/")
    public Song[] reloadSongSuggestions(@RequestParam("imageId") Long imageId) {
        Image image = imageRepository.getReferenceById(imageId);
        return songSuggestionService.reloadSongSuggestions(image);
    }

    @GetMapping("/api/songs/metadata/{songId}")
    public SongMetadata getSongMetadata(@PathVariable Long songId) {
        return songSuggestionService.getSongMetadata(songId);
    }
}
