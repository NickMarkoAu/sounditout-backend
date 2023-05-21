package com.staticvoid.fileupload.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.staticvoid.image.domain.Image;
import com.staticvoid.user.domain.ApplicationUser;
import com.staticvoid.util.AwsCredentials;
import com.staticvoid.util.AwsUtil;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static com.staticvoid.util.AwsUtil.BUCKET_NAME;

public class S3StorageService {

    private final AmazonS3Client s3 = (AmazonS3Client)AmazonS3ClientBuilder.standard()
            .withRegion(AwsUtil.REGION)
            .withClientConfiguration(new ClientConfiguration())
            .withCredentials(AwsCredentials.defaultCredentials()).build();

    public Image store(MultipartFile file, ApplicationUser user) {
        try {
            String s3Key = getS3Key(user, FilenameUtils.getExtension(file.getOriginalFilename()));
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + s3Key);
            convFile.mkdirs();
            file.transferTo(convFile);
            Image image = putImage(user, s3Key, convFile);
            convFile.delete();
            return image;
        } catch (Exception e) {
            throw new RuntimeException("Could not upload file", e);
        }
    }

    public Image store(File file, ApplicationUser user) {
        try {
            String s3Key = getS3Key(user, FilenameUtils.getExtension(file.getName()));
            return putImage(user, s3Key, file);
        } catch (Exception e) {
            throw new RuntimeException("Could not upload file", e);
        }
    }

    public Image putImage(ApplicationUser user, String s3Key, File convFile) throws FileNotFoundException {
        InputStream is = new FileInputStream(convFile);
        s3.putObject(BUCKET_NAME, s3Key, is, new ObjectMetadata());
        return s3KeyAndUserToImage(s3Key, user);
    }

    private Image fileNameToImage(String filename) {
        String s3Uri = s3.getResourceUrl(BUCKET_NAME, filename);
        Image image = new Image();
        image.setS3uri(s3Uri);
        return image;
    }

    private Image s3KeyAndUserToImage(String s3Key, ApplicationUser user) {
        Image image = new Image();
        image.setS3uri(s3Key);
        image.setUserId(user.getId());
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

    public File getFileFromS3Key(String s3Key) throws IOException {
        InputStream is = s3.getObject(BUCKET_NAME, s3Key).getObjectContent();
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + s3Key);
        Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file;
    }

    private String getS3Key(ApplicationUser user, String fileExtension) {
        String filename = UUID.randomUUID().toString();
        return user.getId() + "/images/" + filename + "." + fileExtension;
    }

}
