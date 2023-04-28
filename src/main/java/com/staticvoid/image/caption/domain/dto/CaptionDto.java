package com.staticvoid.image.caption.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class CaptionDto implements Serializable {
    private final String text;
    private final double confidence;

    public CaptionDto(@JsonProperty("text") String text, @JsonProperty("confidence") double confidence) {
        this.text = text;
        this.confidence = confidence;
    }
}
