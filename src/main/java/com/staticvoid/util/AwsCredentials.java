package com.staticvoid.util;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;

public class AwsCredentials {

    public static AWSCredentialsProviderChain defaultCredentials() {
        return new AWSCredentialsProviderChain(new ProfileCredentialsProvider("personal-account"));
    }

    public static AwsCredentialsProviderChain defaultCredentialsProvider() {
        return AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider.create("personal-account"))
                .build();
    }
}
