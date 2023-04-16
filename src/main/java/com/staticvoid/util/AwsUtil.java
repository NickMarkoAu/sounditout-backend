package com.staticvoid.util;

import com.amazonaws.auth.AWSCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

public class AwsUtil {
    public static final String REGION = "ap-southeast-2";
    //TODO change to config
    public static final String BUCKET_NAME = "sound-it-out";
    private static final Integer PRESIGNED_URL_VALIDITY = 60 * 2;

    public static URL generatePresignedUrl(String objectKey) {
        Duration expiresIn = Duration.ofMinutes(PRESIGNED_URL_VALIDITY);
        AwsCredentialsProvider credentialsProvider = AwsCredentials.defaultCredentialsProvider();

        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(REGION))
                .credentialsProvider(credentialsProvider)
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(objectKey)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiresIn)
                .getObjectRequest(getObjectRequest)
                .build();

        URL presignedUrl = presigner.presignGetObject(getObjectPresignRequest).url();

        // Close the clients when they are no longer needed
        presigner.close();
        return presignedUrl;
    }
}
