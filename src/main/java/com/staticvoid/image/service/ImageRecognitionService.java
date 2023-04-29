package com.staticvoid.image.service;

import com.staticvoid.image.domain.Image;

import java.io.File;
import java.util.List;

public interface ImageRecognitionService {
    List<String> detectImageLabels(Image image);

    List<String> detectImageLabels(File sourceImage);
}
