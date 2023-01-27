package com.staticvoid.fileupload.service;

import com.staticvoid.image.domain.Image;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    Image store(MultipartFile file);

    String getAsString(String s3Uri);
}
