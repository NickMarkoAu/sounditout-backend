package com.staticvoid.voicetotext.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements Serializable {

    private String start_time;
    private String end_time;
    private String type;
}
