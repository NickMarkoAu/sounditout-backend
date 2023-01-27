package com.staticvoid.voicetotext.api;

import com.staticvoid.fileupload.service.StorageService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.voicetotext.service.VoiceToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TranscribeVoiceController {

    private final StorageService storageService;
    private final VoiceToTextService voiceToTextService;

    @Autowired
    public TranscribeVoiceController(StorageService storageService, VoiceToTextService voiceToTextService) {
        this.storageService = storageService;
        this.voiceToTextService = voiceToTextService;
    }

    @PostMapping("/transcribe")
    public String transcribe(@RequestParam("file") MultipartFile file) throws Exception {
        Image storedFile = storageService.store(file);
        return voiceToTextService.convertAudio(storedFile.getS3uri());
    }
}
