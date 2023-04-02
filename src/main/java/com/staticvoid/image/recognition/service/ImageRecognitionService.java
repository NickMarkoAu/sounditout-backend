package com.staticvoid.image.recognition.service;

import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Label;
import com.staticvoid.fileupload.service.S3StorageService;
import com.staticvoid.image.repository.ImageRepository;
import com.staticvoid.util.AwsCredentials;
import com.staticvoid.util.AwsUtil;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageRecognitionService {
    private final AmazonRekognitionClient rekClient;
    @Autowired
    private ImageRepository imageRepository;
    private static final Integer MAX_LABELS = 15;
    private static final S3StorageService s3StorageService = new S3StorageService();

    public ImageRecognitionService() {
        this.rekClient = (AmazonRekognitionClient) AmazonRekognitionClientBuilder.standard()
                .withCredentials(AwsCredentials.defaultCredentials())
                .withRegion(AwsUtil.REGION)
                .build();
    }

    public List<String> detectImageLabels(com.staticvoid.image.domain.Image image) {
        try {
            //TODO file is already on s3 at this point to it should be faster to use the s3 key and pass that to rekognition rather than get the image and use the file
            List<String> tags = detectImageLabels(s3StorageService.getFileFromS3Key(image.getS3uri()));
            //make tags lowercase and replace spaces with dashes
            tags = tags.stream().map(tag -> tag.toLowerCase().replace(" ", "-")).collect(Collectors.toList());
            image.setTags(JSONArray.toJSONString(tags));
            imageRepository.save(image);
            return tags;
        } catch (Exception e) {
            throw new RuntimeException("Could not detect image labels: ", e);
        }
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
