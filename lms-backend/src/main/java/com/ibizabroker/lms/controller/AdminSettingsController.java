package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.entity.SystemSetting;
import com.ibizabroker.lms.service.SystemSettingService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/settings")
@CrossOrigin("http://localhost:4200/")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminSettingsController {

    private final SystemSettingService settingsService;

    @GetMapping
    public ResponseEntity<List<SystemSetting>> list() {
        return ResponseEntity.ok(settingsService.findAll());
    }

    public static class UpdateSettingRequest {
        @NotBlank
        public String value;
    }

    @PutMapping("/{key}")
    public ResponseEntity<SystemSetting> update(@PathVariable String key, @RequestBody UpdateSettingRequest body) {
        SystemSetting s = settingsService.upsert(key, body.value);
        return ResponseEntity.ok(s);
    }
}
