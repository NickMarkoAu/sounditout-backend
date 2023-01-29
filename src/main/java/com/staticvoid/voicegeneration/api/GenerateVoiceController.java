package com.staticvoid.voicegeneration.api;

import com.staticvoid.fileupload.service.StorageService;
import com.staticvoid.image.domain.Image;
import com.staticvoid.text.service.TextGenerationService;
import com.staticvoid.texttovoice.service.TextToVoiceService;
import com.staticvoid.voicetotext.service.VoiceToTextService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class GenerateVoiceController {

    private final VoiceToTextService voiceToTextService;
    private final TextGenerationService textGenerationService;
    private final TextToVoiceService textToVoiceService;
    private final StorageService storageService;

    @GetMapping("/voice/")
    public File generateVoiceFromVoicePrompt(@RequestParam("file") MultipartFile file) throws Exception {
        Image storedFile = storageService.store(file);
        String transcribedVoice = voiceToTextService.convertAudio(storedFile.getS3uri());
        String result = textGenerationService.generateFromPrompt(transcribedVoice);
        File resultFile = new File("result.mp3");
        InputStream is = textToVoiceService.synthesize(result);
        try {
            FileUtils.copyInputStreamToFile(is, resultFile);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file", e);
        }
        return resultFile;
    }
}
