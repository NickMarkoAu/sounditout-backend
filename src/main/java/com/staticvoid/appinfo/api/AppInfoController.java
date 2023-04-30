package com.staticvoid.appinfo.api;

import com.staticvoid.appinfo.service.AppInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AppInfoController {

    private final AppInfoService appInfoService;

    @GetMapping("/api/appinfo")
    public ResponseEntity<?> getAppInfo() {
        try {
            return ResponseEntity.ok(appInfoService.getAppInfo());
        } catch (Exception e) {
            log.error("Could not return app info: ", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
