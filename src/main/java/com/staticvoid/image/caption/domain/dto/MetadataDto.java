package com.staticvoid.image.caption.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class MetadataDto implements Serializable {
    private int width;
    private int height;
}
