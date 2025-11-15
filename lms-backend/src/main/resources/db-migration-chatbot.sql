-- SQL Migration for RAG Chatbot Implementation
-- Run this in your MySQL database to set up the chat_messages table

-- Create chat_messages table for storing conversation history
CREATE TABLE IF NOT EXISTS chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique message ID',
    conversation_id VARCHAR(255) NOT NULL COMMENT 'UUID for conversation grouping',
    user_id INT NOT NULL COMMENT 'ID of the user who sent the message',
    user_message TEXT NOT NULL COMMENT 'The message from the user',
    bot_response LONGTEXT NOT NULL COMMENT 'The response from the bot',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'When the message was created',
    
    -- Indexes for performance
    KEY idx_conversation (conversation_id) COMMENT 'Index for conversation lookups',
    KEY idx_user_id (user_id) COMMENT 'Index for user-specific queries',
    KEY idx_created_at (created_at) COMMENT 'Index for time-based queries'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Stores chatbot conversation messages with RAG context';

-- Add foreign key constraint if users table exists
ALTER TABLE chat_messages 
    ADD CONSTRAINT fk_chat_messages_user_id 
    FOREIGN KEY (user_id) REFERENCES users(user_id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE;

-- Create indexes for common queries
CREATE INDEX idx_conversation_created ON chat_messages(conversation_id, created_at DESC);
CREATE INDEX idx_user_created ON chat_messages(user_id, created_at DESC);

-- Verify table creation
SELECT TABLE_NAME, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_NAME = 'chat_messages' AND TABLE_SCHEMA = DATABASE();
