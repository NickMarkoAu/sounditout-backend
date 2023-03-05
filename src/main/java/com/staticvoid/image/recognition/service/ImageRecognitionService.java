package com.staticvoid.image.recognition.service;

import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Label;
import com.staticvoid.image.repository.ImageRepository;
import com.staticvoid.util.AwsCredentials;
import com.staticvoid.util.AwsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ImageRecognitionService {
    private final AmazonRekognitionClient rekClient;
    @Autowired
    private ImageRepository imageRepository;
    private static final Integer MAX_LABELS = 15;

    public ImageRecognitionService() {
        this.rekClient = (AmazonRekognitionClient) AmazonRekognitionClientBuilder.standard()
                .withCredentials(AwsCredentials.defaultCredentials())
                .withRegion(AwsUtil.REGION)
                .build();
    }

    public List<String> detectImageLabels(com.staticvoid.image.domain.Image image) {
        List<String> tags = detectImageLabels(image.getFile());
        image.setTags(tags.toString());
        imageRepository.save(image);
        return tags;
    }

    public List<String> detectImageLabels(File sourceImage) {
        List<String> result = new ArrayList<>();
        try {
            InputStream sourceStream = new FileInputStream(sourceImage);
            ByteBuffer sourceBytes = SdkBytes.fromInputStream(sourceStream).asByteBuffer();

            // Create an Image object for the source image.
            Image image = new Image();
            image.setBytes(sourceBytes);

            DetectLabelsRequest detectLabelsRequest = new DetectLabelsRequest();
            detectLabelsRequest.setImage(image);
            detectLabelsRequest.setMaxLabels(MAX_LABELS);

            DetectLabelsResult labelsResponse = rekClient.detectLabels(detectLabelsRequest);
            List<Label> labels = labelsResponse.getLabels();

            log.info("Detected labels for the given photo");
            for (Label label : labels) {
                log.info(label.getName() + ": " + label.getConfidence().toString());
                result.add(label.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not analyse image", e);
        }
        return result;
    }


}
