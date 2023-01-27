package com.staticvoid.util;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class AwsCredentials {

    public static AWSCredentialsProviderChain defaultCredentials() {
        return new AWSCredentialsProviderChain(new ProfileCredentialsProvider("personal-account"));
    }
}
