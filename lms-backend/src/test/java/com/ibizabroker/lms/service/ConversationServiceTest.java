package com.ibizabroker.lms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ConversationServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private ConversationService conversationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSaveHistoryToCache() {
        String conversationId = "12345";
        String history = "User: Hello\nAssistant: Hi!";

        conversationService.saveHistoryToCache(conversationId, history);

        verify(valueOperations, times(1)).set(
            eq("conversation:" + conversationId),
            eq(history),
            eq(60L),
            eq(java.util.concurrent.TimeUnit.MINUTES)
        );
    }

    @Test
    void testGetHistoryFromCache() {
        String conversationId = "12345";
        String expectedHistory = "User: Hello\nAssistant: Hi!";

        when(valueOperations.get("conversation:" + conversationId)).thenReturn(expectedHistory);

        String actualHistory = conversationService.getHistoryFromCache(conversationId);

        assertEquals(expectedHistory, actualHistory);
        verify(valueOperations, times(1)).get("conversation:" + conversationId);
    }

    @Test
    void testClearHistoryFromCache() {
        String conversationId = "12345";

        conversationService.clearHistoryFromCache(conversationId);

        verify(redisTemplate, times(1)).delete("conversation:" + conversationId);
    }
}