package com.staticvoid.image.caption.service;

import com.staticvoid.fileupload.service.S3StorageService;
import com.staticvoid.image.caption.domain.dto.CaptionResponseDto;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.domain.dto.ImageDto;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class CaptionServiceTest {

    @Autowired
    CaptionService service;

    private final S3StorageService storageService = new S3StorageService();


    @Test
    void should_caption_image() throws Exception {
        File file = Paths.get("src", "test", "resources", "img", "test-image.jpg").toFile();
        ApplicationUser user = new ApplicationUser();
        user.setId(12345L);
        Image image = storageService.putImage(user, "test-image.jpg", file);
        ImageDto imageDto = ImageDto.toDto(image);

        String presignedUrl = imageDto.getPresignedUrl();
        log.info("Presigned URL: {}", presignedUrl);
        CaptionResponseDto captionResponseDto = service.generateCaption(presignedUrl);
        log.info("Response: {}", captionResponseDto);
        String caption = captionResponseDto.getCaptionResult().getText();

        assertThat(caption).isNotBlank();
    }
}