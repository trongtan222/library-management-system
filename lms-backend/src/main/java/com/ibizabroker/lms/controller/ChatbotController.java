package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dto.ChatRequestDto;
import com.ibizabroker.lms.service.ConversationService;
import com.ibizabroker.lms.service.RagService;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*; // Sửa lại import
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException; // ✅ ĐÃ SỬA
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/chat")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class ChatbotController {

    @Value("${gemini.api.key}")
    private String apiKey;
    @Value("${gemini.service.account.path:}")
    private String serviceAccountPath;

    // ⭐ Đã thay đổi sang RestTemplate
    private final RestTemplate restTemplate;
    private final RagService ragService;
    private final ConversationService conversationService;

    /**
     * Main chat endpoint with RAG support (ĐÃ CHUYỂN SANG ĐỒNG BỘ)
     */
    @PostMapping
    // ⭐ Đã bỏ 'Mono'
    public ResponseEntity<String> ask(@Valid @RequestBody ChatRequestDto chatRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logAuthenticationDetails(auth);

        String conversationId = chatRequest.getConversationId() != null ?
                chatRequest.getConversationId() :
                conversationService.generateConversationId();

        Integer userId = extractUserId(auth);

        // Check Redis cache for conversation history
        String history = conversationService.getHistoryFromCache(conversationId);
        if (history == null) {
            history = conversationService.buildConversationContext(conversationId);
            conversationService.saveHistoryToCache(conversationId, history);
        }

        // Build context-aware prompt
        String augmentedPrompt = history + "\nUser: " + chatRequest.getPrompt();

        // Call AI service (existing logic)
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";
        var payload = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", augmentedPrompt)
                                }
                        )
                }
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String bearerToken = null;
        if (serviceAccountPath != null && !serviceAccountPath.isBlank()) {
            try (FileInputStream fis = new FileInputStream(serviceAccountPath)) {
                GoogleCredentials creds = GoogleCredentials.fromStream(fis)
                        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
                AccessToken at = creds.refreshAccessToken();
                if (at != null) bearerToken = at.getTokenValue();
            } catch (Exception ex) {
                logger.warn("Failed to load service account credentials: " + ex.getMessage());
            }
        }

        String finalUrl;
        if (bearerToken != null) {
            headers.set("Authorization", "Bearer " + bearerToken);
            finalUrl = url;
        } else {
            finalUrl = url + "?key=" + apiKey;
        }

        HttpEntity<?> requestEntity = new HttpEntity<>(payload, headers);
        try {
            ResponseEntity<String> geminiResponse = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String geminiJsonResponse = geminiResponse.getBody();

            // Save message and update Redis cache
            conversationService.saveMessage(conversationId, userId, chatRequest.getPrompt(), geminiJsonResponse);
            conversationService.saveHistoryToCache(conversationId, history + "\nUser: " + chatRequest.getPrompt() + "\nAssistant: " + geminiJsonResponse);

            return ResponseEntity.ok()
                    .header("X-Conversation-Id", conversationId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(geminiJsonResponse);

        } catch (Exception e) {
            String errorMsg = handleApiError(e);
            logger.error("Chatbot API error: ", e);
            String errorJson = "{\"error\": \"" + errorMsg.replace("\"", "\\\"") + "\"}";

            return ResponseEntity.status(500)
                    .header("X-Conversation-Id", conversationId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorJson);
        }
    }

    /**
     * Get conversation history
     */
    @GetMapping("/history/{conversationId}")
    public ResponseEntity<String> getConversationHistory(@PathVariable String conversationId) {
        // Code này đã đồng bộ, không cần thay đổi
        try {
            var history = conversationService.getConversationHistory(conversationId);
            // Cân nhắc chuyển đổi 'history' sang JSON string thay vì .toString()
            return ResponseEntity.ok(history.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Failed to retrieve conversation history\"}");
        }
    }

    /**
     * Get list of user's conversations
     */
    @GetMapping("/conversations")
    public ResponseEntity<String> getUserConversations() {
        // Code này đã đồng bộ, không cần thay đổi
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Integer userId = extractUserId(auth);

            var conversations = conversationService.getUserConversations(userId);
            // Cân nhắc chuyển đổi 'conversations' sang JSON string thay vì .toString()
            return ResponseEntity.ok(conversations.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Failed to retrieve conversations\"}");
        }
    }

    /**
     * Extract user ID from authentication with type safety check
     */
    private Integer extractUserId(Authentication auth) {
        if (auth == null) {
            logger.warn("Authentication object is null, cannot extract user ID.");
            return null;
        }
        try {
            Object principal = auth.getPrincipal();
            if (principal instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> details = (Map<String, Object>) principal;
                Object userId = details.get("userId");
                if (userId instanceof Integer) {
                    return (Integer) userId;
                }
            }
        } catch (Exception e) {
            logger.warn("Could not extract userId from auth principal: " + auth.getPrincipal(), e);
        }
        logger.warn("Could not extract userId from principal of type: " + auth.getPrincipal().getClass().getName());
        return null;
    }

    /**
     * Handle different API errors
     */
    private String handleApiError(Throwable e) {
        // ✅ ĐÃ SỬA
        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException we = (HttpStatusCodeException) e;
            logger.warn("API Error: {} - {}", we.getStatusCode(), we.getResponseBodyAsString());
            if (we.getStatusCode().value() == 429) {
                return "Too many requests. Please wait a moment and try again.";
            }
            if (we.getStatusCode().value() == 401) {
                return "API key is invalid. Please contact support.";
            }
            if (we.getStatusCode().value() == 404) {
                return "Chatbot model not found. Please contact support. (404)";
            }
            if (we.getStatusCode().value() >= 500) {
                return "AI service is temporarily unavailable. Please try again later.";
            }
        }
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            if (e.getCause().getMessage().contains("timeout")) {
                return "Request timed out. Please try again.";
            }
        }
        return "Sorry, I encountered an error processing your request. Please try again.";
    }

    /**
     * Debugging method to log authentication details
     */
    private void logAuthenticationDetails(Authentication auth) {
        if (auth == null) {
            logger.warn("Authentication object is null.");
            return;
        }
        logger.info("Authentication details: Principal={}, Authorities={}", auth.getPrincipal(), auth.getAuthorities());
    }

    // Logger for this class
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(ChatbotController.class);
}