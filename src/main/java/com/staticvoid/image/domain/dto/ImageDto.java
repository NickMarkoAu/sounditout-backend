package com.staticvoid.image.domain.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticvoid.image.domain.Image;
import com.staticvoid.util.AwsUtil;
import com.staticvoid.util.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;

import static com.staticvoid.security.jwt.JwtTokenUtil.JWT_TOKEN_VALIDITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto implements Serializable {

    private Long id;
    private String fileName;
    private Long userId;
    private String[] tags;
    private File file;
    private String presignedUrl;

    public Image toEntity() {
        return new Image(id, fileName, userId, JSONArray.toJSONString(Arrays.asList(tags)), file);
    }

    public static ImageDto toDto(Image image) {
        ObjectMapper mapper = new ObjectMapper();
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setFileName(image.getFileName());
        imageDto.setUserId(image.getUserId());
        //get presigned URL from S3
        imageDto.setPresignedUrl(AwsUtil.generatePresignedUrl(image.getS3uri()).toString());
        if(image.getTags() != null) {
            try {
                imageDto.setTags(mapper.readValue(image.getTags(), String[].class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Could not read tags value from Image entity", e);
            }
        }
        imageDto.setFile(image.getFile());
        return imageDto;
    }
}
