package com.staticvoid.voicetotext.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmazonTranscription implements Serializable {

    private String jobName;
    private String accountId;
    private Result results;
    private String status;
}

