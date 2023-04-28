package com.staticvoid.image.caption.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ResultPageDto implements Serializable {
    private double height;
    private double width;
    private double angle;
    private int pageNumber;
    private WordDto[] words;
    private SpanDto[] spans;
    private LineDto[] lines;
}
