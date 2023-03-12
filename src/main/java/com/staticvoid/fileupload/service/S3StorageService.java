package com.staticvoid.fileupload.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.staticvoid.image.domain.Image;
import com.staticvoid.util.AwsCredentials;
import com.staticvoid.util.AwsUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

import static com.staticvoid.util.AwsUtil.BUCKET_NAME;

public class S3StorageService {

    private final AmazonS3Client s3 = (AmazonS3Client)AmazonS3ClientBuilder.standard()
            .withRegion(AwsUtil.REGION)
            .withClientConfiguration(new ClientConfiguration())
            .withCredentials(AwsCredentials.defaultCredentials()).build();

    public Image store(MultipartFile file) {
        try {
            String filename = UUID.randomUUID().toString();
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + filename);
            file.transferTo(convFile);
            s3.putObject(BUCKET_NAME, filename, new File(convFile.getName()));
            return fileNameToImage(filename);
        } catch (Exception e) {
            throw new RuntimeException("Could not upload file", e);
        }
    }

    private Image fileNameToImage(String filename) {
        String s3Uri = s3.getResourceUrl(BUCKET_NAME, filename);
        Image image = new Image();
        image.setFileName(filename);
        image.setS3uri(s3Uri);
        return image;
    }

    public S3Object getS3Object(String s3Uri) {
        try {
            if (s3Uri.startsWith("/")) {
                s3Uri = s3Uri.substring(1);
            }
            if (s3Uri.endsWith("/")) {
                s3Uri = s3Uri.substring(0, s3Uri.length());
            }
            return s3.getObject(BUCKET_NAME, s3Uri);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String getS3Key() {
        String s3Key = "";

        return s3Key;
    }

}
