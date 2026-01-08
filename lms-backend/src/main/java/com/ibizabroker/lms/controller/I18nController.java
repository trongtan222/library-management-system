package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.service.I18nService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Feature 6: i18n Controller
 */
@RestController
@RequestMapping("/api/public/i18n")
@RequiredArgsConstructor
public class I18nController {

    private final I18nService i18nService;

    @GetMapping("/messages")
    public ResponseEntity<Map<String, String>> getAllMessages(
            @RequestParam(defaultValue = "vi") String lang) {
        return ResponseEntity.ok(i18nService.getAllMessages(lang));
    }

    @GetMapping("/message/{key}")
    public ResponseEntity<Map<String, String>> getMessage(
            @PathVariable String key,
            @RequestParam(defaultValue = "vi") String lang) {
        String message = i18nService.getMessage(key, lang);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
