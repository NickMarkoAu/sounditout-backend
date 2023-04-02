package com.staticvoid.image.recognition.service;

import com.staticvoid.image.domain.Image;
import com.staticvoid.image.repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow();
    }
}
