package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.ChatMessageRepository;
import com.ibizabroker.lms.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationService {
    
    private final ChatMessageRepository chatMessageRepository;
    private final StringRedisTemplate redisTemplate;

    private static final long CACHE_EXPIRATION_MINUTES = 60;

    /**
     * Generate a new conversation ID
     */
    public String generateConversationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Save a chat message exchange
     */
    public ChatMessage saveMessage(String conversationId, Integer userId, String userMessage, String botResponse) {
        ChatMessage message = new ChatMessage(conversationId, userId, userMessage, botResponse);
        return chatMessageRepository.save(message);
    }

    /**
     * Save conversation history to Redis cache
     */
    public void saveHistoryToCache(String conversationId, String history) {
        redisTemplate.opsForValue().set(
            "conversation:" + conversationId,
            history,
            CACHE_EXPIRATION_MINUTES,
            TimeUnit.MINUTES
        );
    }

    /**
     * Get conversation history from Redis cache
     */
    public String getHistoryFromCache(String conversationId) {
        return redisTemplate.opsForValue().get("conversation:" + conversationId);
    }

    /**
     * Clear conversation history from Redis cache
     */
    public void clearHistoryFromCache(String conversationId) {
        redisTemplate.delete("conversation:" + conversationId);
    }

    /**
     * Get conversation history
     */
    @Transactional(readOnly = true)
    public List<ChatMessage> getConversationHistory(String conversationId) {
        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    /**
     * Get recent messages for context (up to 5 last messages)
     */
    /**
     * Xây dựng ngữ cảnh hội thoại (System Prompt + Lịch sử)
     */
    @Transactional(readOnly = true)
    public String buildConversationContext(String conversationId) {
        // 1. Định nghĩa "nhân cách" và quy định của trường
        String systemContext = """
            Bối cảnh: Bạn là Trợ lý ảo AI thông minh của Thư viện Trường Trung học cơ sở Phương Tú (Ứng Hòa, Hà Nội).
            Đối tượng phục vụ: Học sinh từ lớp 6 đến lớp 9 và Giáo viên nhà trường.
            
            Nhiệm vụ của bạn:
            1. Giới thiệu sách phù hợp lứa tuổi thiếu niên (Sách giáo khoa, Văn học tuổi xanh, Sách tham khảo, Kỹ năng sống).
            2. Giải đáp quy định thư viện một cách thân thiện, lễ phép.
            
            Quy định thư viện THCS Phương Tú:
            - Thời gian mở cửa: 7h30 - 16h30 (Thứ 2 đến Thứ 6).
            - Học sinh được mượn tối đa: 3 cuốn / lần.
            - Thời hạn mượn: 14 ngày.
            - Làm mất sách: Phải đền sách mới hoặc tiền tương đương giá bìa + 20% phí xử lý.
            
            Lưu ý khi trả lời:
            - Luôn xưng hô là "mình" với các bạn học sinh, và "em/con" nếu người hỏi xưng là thầy cô.
            - Văn phong: Trong sáng, khuyến khích đọc sách, không dùng từ ngữ quá chuyên ngành.
            """;

        // 2. Lấy lịch sử chat cũ (nếu có)
        List<ChatMessage> recentMessages = chatMessageRepository.findRecentMessages(conversationId);
        
        if (recentMessages.isEmpty()) {
            return systemContext + "\n\nĐây là bắt đầu cuộc hội thoại.";
        }
        
        StringBuilder context = new StringBuilder(systemContext + "\n\nLịch sử chat gần đây:\n");
        for (int i = recentMessages.size() - 1; i >= 0; i--) {
            ChatMessage msg = recentMessages.get(i);
            context.append("User: ").append(msg.getUserMessage()).append("\n");
            context.append("Assistant: ").append(msg.getBotResponse()).append("\n");
        }
        
        return context.toString();
    }

    /**
     * Get all conversations for a user
     */
    @Transactional(readOnly = true)
    public List<String> getUserConversations(Integer userId) {
        return chatMessageRepository.findUserConversationIds(userId);
    }

    /**
     * Build augmented prompt with conversation context
     */
    public String buildContextAwarePrompt(String userQuery, String conversationId) {
        String conversationContext = buildConversationContext(conversationId);
        
        return conversationContext + "\n" +
               "Current user question: " + userQuery + "\n\n" +
               "Please provide a helpful response based on the conversation context and the user's current question.";
    }
}
