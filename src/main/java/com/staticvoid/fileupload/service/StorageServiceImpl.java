package com.staticvoid.fileupload.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.repository.ImageRepository;
import com.staticvoid.util.AwsCredentials;
import com.staticvoid.util.AwsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.staticvoid.util.AwsUtil.BUCKET_NAME;

@Service
public class StorageServiceImpl implements StorageService {

    private AmazonS3Client s3;
    private ImageRepository imageRepository;

    public StorageServiceImpl() {
        init();
    }

    @Override
    public void init() {
        s3 = (AmazonS3Client)AmazonS3ClientBuilder.standard()
                .withRegion(AwsUtil.REGION)
                .withClientConfiguration(new ClientConfiguration())
                .withCredentials(AwsCredentials.defaultCredentials()).build();
    }

    @Override
    public Image store(MultipartFile file) {
        try {
            String filename = UUID.randomUUID().toString();
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + filename);
            file.transferTo(convFile);
            s3.putObject(BUCKET_NAME, filename, new File(convFile.getName()));

            return persist(filename);
        } catch (Exception e) {
            throw new RuntimeException("Could not upload file", e);
        }
    }

    private Image persist(String filename) {
        String s3Uri = s3.getResourceUrl(BUCKET_NAME, filename);
        Image image = new Image();
        image.setFileName(filename);
        image.setS3uri(s3Uri);
        return imageRepository.save(image);
    }

    @Override
    public String getAsString(String s3Uri) {
        try {
            if (s3Uri.startsWith("/")) {
                s3Uri = s3Uri.substring(1);
            }
            if (s3Uri.endsWith("/")) {
                s3Uri = s3Uri.substring(0, s3Uri.length());
            }
            try (InputStream is = s3.getObject(BUCKET_NAME, s3Uri).getObjectContent()) {
                return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
