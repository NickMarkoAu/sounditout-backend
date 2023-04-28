package com.staticvoid.image.caption.service;

import com.staticvoid.image.caption.domain.dto.CaptionRequestDto;
import com.staticvoid.image.caption.domain.dto.CaptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CaptionService {
    private final String subscriptionKey;
    private final String endpoint;

    private final RestTemplate restTemplate;

    public CaptionService(@Value("${microsoft.computer-vision.subscription-key}") String subscriptionKey,
                          @Value("${microsoft.computer-vision.endpoint}") String endpoint,
                          @Autowired RestTemplate restTemplate) {
        this.subscriptionKey = subscriptionKey;
        this.endpoint = endpoint;
        this.restTemplate = restTemplate;
    }

    public CaptionResponseDto generateCaption(String url) {
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




}
