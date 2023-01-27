package com.staticvoid.texttovoice.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import  com.amazonaws.regions.Region;
import com.staticvoid.util.AwsCredentials;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class TextToVoiceService {

    private final AmazonPollyClient polly;
    private final Voice voice;
    private static final Region REGION = Region.getRegion(Regions.AP_SOUTHEAST_2);

    public TextToVoiceService() {
        polly = new AmazonPollyClient(AwsCredentials.defaultCredentials(),
                new ClientConfiguration());
        polly.setRegion(REGION);
        // Create describe voices request.
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

        // Synchronously ask Amazon Polly to describe available TTS voices.
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        voice = describeVoicesResult.getVoices().get(0);
    }

    public InputStream synthesize(String text) {
        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
                        .withOutputFormat(OutputFormat.Mp3).withEngine("neural");
        SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

        return synthRes.getAudioStream();
    }
    

}
