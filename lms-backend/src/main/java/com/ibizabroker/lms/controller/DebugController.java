package com.ibizabroker.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/whoami")
    public ResponseEntity<Map<String, Object>> whoami(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> out = new HashMap<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            out.put("securityContext", null);
        } else {
            Map<String, Object> sec = new HashMap<>();
            sec.put("authenticated", auth.isAuthenticated());
            sec.put("principal", auth.getPrincipal() == null ? null : auth.getPrincipal().toString());
            sec.put("name", auth.getName());
            sec.put("authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            out.put("securityContext", sec);
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            out.put("tokenPresent", true);
            try {
                String[] parts = token.split("\\.");
                if (parts.length == 3) {
                    String payload = parts[1];
                    // Base64 URL decode
                    int mod = payload.length() % 4;
                    if (mod != 0) payload += "=".repeat(4 - mod);
                    byte[] decoded = Base64.getUrlDecoder().decode(payload);
                    String json = new String(decoded);
                    out.put("tokenPayload", json);
                } else {
                    out.put("tokenPayload", "not_jwt_format");
                }
            } catch (Exception e) {
                out.put("tokenPayloadError", e.getMessage());
            }
        } else {
            out.put("tokenPresent", false);
        }

        return ResponseEntity.ok(out);
    }
}
