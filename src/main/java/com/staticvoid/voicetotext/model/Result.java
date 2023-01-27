package com.staticvoid.voicetotext.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result implements Serializable {

    private List<Transcript> transcripts = new ArrayList<Transcript>();
    private List<Item> items = new ArrayList<Item>();
}
