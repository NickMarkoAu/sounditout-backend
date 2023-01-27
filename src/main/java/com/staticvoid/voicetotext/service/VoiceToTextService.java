package com.staticvoid.voicetotext.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.util.AwsCredentials;
import com.staticvoid.util.AwsUtil;
import com.staticvoid.voicetotext.model.AmazonTranscription;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VoiceToTextService {

    private final AmazonTranscribeClient client;

    public VoiceToTextService() {
        client = (AmazonTranscribeClient) AmazonTranscribeClientBuilder.standard()
                .withCredentials(AwsCredentials.defaultCredentials())
                .withRegion(AwsUtil.REGION)
                .build();
    }

    public String convertAudio(String file) {
        String transcriptionJobName = UUID.randomUUID().toString();

        StartTranscriptionJobRequest request = new StartTranscriptionJobRequest();
        request.withLanguageCode(LanguageCode.EnAU);
        Media media = new Media();
        media.setMediaFileUri(file);
        request.withMedia(media);
        request.setTranscriptionJobName(transcriptionJobName);
        request.withMediaFormat("wav");
        client.startTranscriptionJob(request);

        GetTranscriptionJobRequest jobRequest = new GetTranscriptionJobRequest();

        jobRequest.setTranscriptionJobName(transcriptionJobName);
        TranscriptionJob transcriptionJob;

        String transcriptionUri = null;

        while (true) {
            transcriptionJob = client.getTranscriptionJob(jobRequest).getTranscriptionJob();
            if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name())) {
                transcriptionUri = transcriptionJob.getTranscript().getTranscriptFileUri();
                break;
            } else if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.FAILED.name())) {
                break;
            }

            synchronized (this) {
                try {
                    this.wait(500);
                } catch (InterruptedException ignored) {
                }
            }
        }
        String result = "";
        try {
            result = download(transcriptionUri).getResults().getTranscripts().get(0).getTranscript();
        } catch (Exception e) {
            throw new RuntimeException("Could not get transcript", e);
        }

        return result;
    }

    private AmazonTranscription download(String uri) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        HttpResponse httpresponse = httpclient.execute(httpget);

        String result = new BufferedReader(
                new InputStreamReader(httpresponse.getEntity().getContent(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result, AmazonTranscription.class);
    }


}
