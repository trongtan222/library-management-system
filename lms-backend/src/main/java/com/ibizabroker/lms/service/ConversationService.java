package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.ChatMessageRepository;
import com.ibizabroker.lms.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationService {
    
    private final ChatMessageRepository chatMessageRepository;

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
     * Get conversation history
     */
    @Transactional(readOnly = true)
    public List<ChatMessage> getConversationHistory(String conversationId) {
        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    /**
     * Get recent messages for context (up to 5 last messages)
     */
    @Transactional(readOnly = true)
    public String buildConversationContext(String conversationId) {
        List<ChatMessage> recentMessages = chatMessageRepository.findRecentMessages(conversationId);
        
        if (recentMessages.isEmpty()) {
            return "This is the start of the conversation.";
        }
        
        StringBuilder context = new StringBuilder("Recent conversation history:\n");
        // Reverse to show in chronological order
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
