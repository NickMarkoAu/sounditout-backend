package com.staticvoid.appinfo.service;

import com.staticvoid.appinfo.domain.dto.AppInfoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppInfoService {

    private String appVersion = "";
    private final boolean alphaMode;

    public AppInfoService(@Value("${spring.application.isAlphaMode}") boolean alphaMode) {
        Package appPackage = getClass().getPackage();
        this.appVersion = appPackage.getImplementationVersion() != null ? appPackage.getImplementationVersion() : "DEVELOPMENT";
        this.alphaMode = alphaMode;
    }

    public AppInfoDto getAppInfo() {
        AppInfoDto appInfoDto = new AppInfoDto();
        appInfoDto.setAppVersion(this.appVersion);
        appInfoDto.setAlphaMode(this.alphaMode);
        return appInfoDto;
    }
}
