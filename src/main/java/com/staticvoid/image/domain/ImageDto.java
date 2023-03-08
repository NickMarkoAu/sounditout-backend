package com.staticvoid.image.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto implements Serializable {

    private Long id;
    private String fileName;
    private String userId;
    private String s3uri;
    private String[] tags;
    private File file;

    public Image toEntity() {
        return new Image(id, fileName, userId, s3uri, Arrays.toString(tags), file);
    }

    public static ImageDto toDto(Image image) {
        ObjectMapper mapper = new ObjectMapper();
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setFileName(image.getFileName());
        imageDto.setUserId(image.getUserId());
        imageDto.setS3uri(image.getS3uri());
        try {
            imageDto.setTags(mapper.readValue(image.getTags(), String[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not read tags value from Image entity", e);
        }
        imageDto.setFile(image.getFile());
        return imageDto;
    }
}
