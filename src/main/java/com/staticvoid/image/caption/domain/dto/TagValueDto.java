package com.staticvoid.image.caption.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class TagValueDto implements Serializable {
    private String name;
    private double confidence;
}
