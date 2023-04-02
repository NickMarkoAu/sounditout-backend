package com.staticvoid.image.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.util.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto implements Serializable {

    private Long id;
    private String fileName;
    private Long userId;
    private byte[] imageContent;
    private String[] tags;
    private File file;

    public Image toEntity() {
        return new Image(id, fileName, userId, JSONArray.toJSONString(Arrays.asList(tags)), file);
    }

    public static ImageDto toDto(Image image) {
        ObjectMapper mapper = new ObjectMapper();
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setFileName(image.getFileName());
        imageDto.setUserId(image.getUserId());
        //get from s3 and serve up base 64 byte array to display on front end
        try {
            imageDto.setImageContent(ImageUtil.getByteArrayFromImageS3Bucket(image.getS3uri()));
        } catch(IOException e) {
            throw new RuntimeException("Could not get image from S3", e);
        }
        try {
            imageDto.setTags(mapper.readValue(image.getTags(), String[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not read tags value from Image entity", e);
        }
        imageDto.setFile(image.getFile());
        return imageDto;
    }
}
