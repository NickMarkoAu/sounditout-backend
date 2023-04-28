package com.staticvoid.image.caption.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class WordDto implements Serializable {
    private String content;
    private double[] boundingBox;
    private double confidence;
    private SpanDto span;
}
