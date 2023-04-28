package com.staticvoid.image.caption.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class CaptionResponseDto implements Serializable {
    private CaptionDto captionResult;
    private ReadResultDto readResult;
    private TagsResultDto tagsResult;
    private String modelVersion;
    private MetadataDto metadata;

}
