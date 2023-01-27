package com.staticvoid.voicegeneration.api;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.staticvoid.fileupload.service.StorageService;
import com.staticvoid.text.service.TextGenerationService;
import com.staticvoid.texttovoice.service.TextToVoiceService;
import com.staticvoid.util.AwsCredentials;
import com.staticvoid.voicetotext.service.VoiceToTextService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.staticvoid.util.AwsUtil.BUCKET_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerateVoiceTest {
    private VoiceToTextService voiceToTextService;
    private TextGenerationService textGenerationService;
    private TextToVoiceService textToVoiceService;

    private static final String REGION = "ap-southeast-2";
    private static final String DIRECTORY = "audio/results/";

    @BeforeEach
    void setup() {
        voiceToTextService = new VoiceToTextService();
        textGenerationService = new TextGenerationService();
        textToVoiceService = new TextToVoiceService();
    }
    @Test
    void should_return_prompt_in_voice() {
        String filePath = "s3://staticvoid-openai-testing/audio/generate-one-word-of-text.wav";
        String transcribedVoice = voiceToTextService.convertAudio(filePath);
        String result = textGenerationService.generateFromPrompt(transcribedVoice);
        InputStream is = textToVoiceService.synthesize(result);

        assertEquals(1, result.split(" ").length);

        File file = new File("result-file.mp3");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } catch (IOException e) {
            throw new RuntimeException("Could not copy to file", e);
        }

        AmazonS3Client s3Client = (AmazonS3Client) AmazonS3ClientBuilder.standard().withCredentials(AwsCredentials.defaultCredentials()).withRegion(REGION).build();
        s3Client.putObject(BUCKET_NAME, DIRECTORY + "should_return_prompt_in_voice" + UUID.randomUUID() + ".mp3", file);
    }
}