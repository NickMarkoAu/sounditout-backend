package com.staticvoid.image.caption.service;

import com.staticvoid.fileupload.service.S3StorageService;
import com.staticvoid.image.caption.domain.dto.CaptionRequestDto;
import com.staticvoid.image.caption.domain.dto.CaptionResponseDto;
import com.staticvoid.image.caption.domain.dto.TagValueDto;
import com.staticvoid.image.domain.Image;
import com.staticvoid.image.domain.dto.ImageDto;
import com.staticvoid.image.service.ImageRecognitionService;
import com.staticvoid.user.domain.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaptionService implements ImageRecognitionService {
    private final String subscriptionKey;
    private final String endpoint;
    private final S3StorageService storageService = new S3StorageService();

    private final RestTemplate restTemplate;

    public CaptionService(@Value("${microsoft.computer-vision.subscription-key}") String subscriptionKey,
                          @Value("${microsoft.computer-vision.endpoint}") String endpoint,
                          @Autowired RestTemplate restTemplate) {
        this.subscriptionKey = subscriptionKey;
        this.endpoint = endpoint;
        this.restTemplate = restTemplate;
    }

    public CaptionResponseDto generateCaption(Image image) {
        ImageDto imageDto = ImageDto.toDto(image);

        String presignedUrl = imageDto.getPresignedUrl();
        log.info("Presigned URL: {}", presignedUrl);
        return generateCaption(presignedUrl);
    }

    private CaptionResponseDto generateCaption(String url) {
        CaptionRequestDto request = new CaptionRequestDto(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Ocp-Apim-Subscription-Key", subscriptionKey);

        HttpEntity<CaptionRequestDto> entity = new HttpEntity<>(request, headers);
        String built = buildUrl();
        log.info("Built URL: {}", built);
        CaptionResponseDto captionResponseDto = restTemplate.postForObject(built, entity, CaptionResponseDto.class);
        log.info("Response: {}", captionResponseDto);
        return captionResponseDto;
    }

    private String buildUrl() {
        return endpoint + "computervision/imageanalysis:analyze?features=caption,tags&model-version=latest&language=en&api-version=2023-02-01-preview";
    }


    @Override
    public List<String> detectImageLabels(Image image) {
        ImageDto imageDto = ImageDto.toDto(image);

        String presignedUrl = imageDto.getPresignedUrl();
        CaptionResponseDto captionResponse = generateCaption(presignedUrl);

        return Arrays.stream(captionResponse.getTagsResult().getValues()).map(TagValueDto::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> detectImageLabels(File sourceImage) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Image image = storageService.store(sourceImage, user);

        return detectImageLabels(image);
    }
}
