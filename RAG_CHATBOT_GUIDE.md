# RAG Chatbot Implementation Guide

## Overview

The chatbot has been upgraded with Retrieval-Augmented Generation (RAG) capabilities. It now:
- Retrieves relevant library data for context
- Maintains conversation history
- Provides library-aware answers
- Handles errors gracefully
- Supports multi-turn conversations

## What is RAG?

**Retrieval-Augmented Generation** combines:
1. **Retrieval**: Finding relevant documents/data from your knowledge base (library books in this case)
2. **Augmentation**: Adding that context to the AI prompt
3. **Generation**: Generating better responses using the augmented prompt

## Architecture

### Backend Components

#### 1. **RagService** (`RagService.java`)
Retrieves library context based on user queries.

```java
// Example usage:
String context = ragService.retrieveContext("Harry Potter");
// Returns: "Relevant books in our library:\n- Harry Potter by J.K. Rowling..."

String augmentedPrompt = ragService.buildAugmentedPrompt(userQuery);
// Adds library context to the prompt
```

**Features:**
- Keyword-based book search (by title, author, category)
- Returns up to 5 most relevant books
- Formats context for AI consumption

**Future Improvements:**
- Vector embeddings for semantic search
- Similarity scoring
- Category and genre-based filtering
- Availability filtering

#### 2. **ChatMessage Entity** (`ChatMessage.java`)
Stores conversation history in database.

```sql
CREATE TABLE chat_messages (
    id BIGINT AUTO_INCREMENT,
    conversation_id VARCHAR(255),
    user_id INT,
    user_message TEXT,
    bot_response TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (id)
);
```

#### 3. **ConversationService** (`ConversationService.java`)
Manages conversation state and history.

```java
// Generate conversation ID
String convId = conversationService.generateConversationId();

// Save message exchange
conversationService.saveMessage(convId, userId, userMsg, botResponse);

// Get conversation history
List<ChatMessage> history = conversationService.getConversationHistory(convId);

// Build context from recent messages
String context = conversationService.buildConversationContext(convId);
```

#### 4. **ChatbotController** (Updated)
Now uses RAG and maintains conversation context.

**Endpoints:**
- `POST /user/chat` - Send message (with RAG)
- `GET /user/chat/history/{conversationId}` - Get conversation history
- `GET /user/chat/conversations` - Get user's conversations

**RAG Flow:**
```
User Message
    ↓
Build RAG-augmented prompt
    ├── Retrieve library context (RagService)
    ├── Get conversation history (ConversationService)
    └── Combine with user query
    ↓
Send to Gemini AI
    ↓
Save to conversation history
    ↓
Return response to frontend
```

### Frontend Components

#### 1. **ChatbotComponent** (Updated)
Enhanced with conversation support.

```typescript
// Conversation ID generation and tracking
this.conversationId = this.generateUUID();

// Send message with conversation context
this.chatbotService.ask(userMessage, this.conversationId)

// Clear chat (start new conversation)
clearChat()

// Proper cleanup with OnDestroy
ngOnDestroy()
```

#### 2. **ChatbotService** (Updated)
Uses centralized API configuration.

```typescript
// Send message with conversation ID
ask(prompt: string, conversationId?: string): Observable<any>

// Get conversation history
getConversationHistory(conversationId: string): Observable<any>

// Get all user conversations
getUserConversations(): Observable<any>
```

#### 3. **UI/UX Improvements**
- Better message formatting (markdown support)
- Typing indicator animation
- Message timestamps
- Clear chat button
- Responsive design for mobile
- Improved error messages

## Database Schema

```sql
CREATE TABLE chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    user_message TEXT NOT NULL,
    bot_response TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Indexes for performance
    INDEX idx_conversation (conversation_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
);
```

## Usage Examples

### Example 1: Book Search with RAG

**User:** "What fantasy books do you have?"

**Backend Process:**
1. RagService searches for books with "fantasy" in category
2. Returns: "Harry Potter, Lord of the Rings, Percy Jackson"
3. Builds augmented prompt with this context
4. Sends to Gemini with context
5. Gemini responds: "We have great fantasy books..."

### Example 2: Conversation Context

**User 1:** "Do you have Harry Potter?"
**Bot:** "Yes, we have Harry Potter series..."

**User 2:** (same conversation) "How many copies?"
**Bot:** "The Harry Potter series has..." (understands context)

### Example 3: Multi-turn Conversation

**Turn 1:**
- User: "I like mystery books"
- Context: Retrieved mystery books
- Bot: "Here are our mystery books..."

**Turn 2:**
- User: "Can I borrow one?"
- Context: Previous turn + mystery books + user profile
- Bot: "Yes, you can borrow 'The Da Vinci Code'..."

## Configuration

### Environment Variables

```env
# Gemini API
GEMINI_API_KEY=your-api-key-here

# Database (for chat history)
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/lms_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=
```

### Application Properties

```properties
# Ensure database initialization includes chat_messages table
spring.jpa.hibernate.ddl-auto=validate
spring.sql.init.mode=always
```

## API Response Examples

### Successful Response

```json
{
  "candidates": [
    {
      "content": {
        "parts": [
          {
            "text": "We have Harry Potter series available..."
          }
        ]
      }
    }
  ]
}
```

### Error Response

```json
{
  "error": "Too many requests. Please wait a moment and try again."
}
```

## Error Handling

The chatbot handles various error scenarios:

| Error | Message | Solution |
|-------|---------|----------|
| 429 (Rate Limit) | "Too many requests" | Wait before retrying |
| 401 (Auth) | "API key is invalid" | Contact support |
| 500+ (Server) | "AI service unavailable" | Retry later |
| Timeout | "Request timed out" | Retry |
| No API Key | "API configuration error" | Set GEMINI_API_KEY env var |

## Limitations & Future Improvements

### Current Limitations
- Keyword-based search (not semantic)
- Simple context augmentation
- No conversation persistence between sessions
- Limited to 5 recent messages for context

### Future Enhancements

#### Phase 1: Vector Embeddings
```java
// Use embeddings for semantic search
List<Book> semanticSearch(String query, int topK)
```

#### Phase 2: Advanced RAG
```java
// Multiple context sources
- User borrowing history
- User reading preferences
- Book reviews and ratings
- Similar books recommendations
```

#### Phase 3: Conversation Memory
```java
// Long-term memory
- Summarize old conversations
- Extract user preferences
- Build user profile
```

#### Phase 4: Multi-modal
```java
// Support rich content
- Book cover images
- Author photos
- Recommendations with ratings
```

## Testing

### Test RAG Functionality

```bash
# 1. Test book search RAG
curl -X POST http://localhost:8080/user/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"prompt": "Do you have any science fiction books?"}'

# 2. Test conversation history
curl -X GET http://localhost:8080/user/chat/history/{conversationId} \
  -H "Authorization: Bearer {token}"

# 3. Get all user conversations
curl -X GET http://localhost:8080/user/chat/conversations \
  -H "Authorization: Bearer {token}"
```

### Frontend Testing

```typescript
// Test conversation tracking
it('should maintain conversation ID', () => {
  const convId = component.conversationId;
  expect(convId).toBeTruthy();
  expect(convId).toMatch(/^[0-9a-f-]+$/);
});

// Test message formatting
it('should format markdown', () => {
  const formatted = component.formatText('**bold** text');
  expect(formatted).toContain('<b>bold</b>');
});

// Test error handling
it('should display error messages', () => {
  const errorMsg = component.extractErrorMessage(error401);
  expect(errorMsg).toContain('Session expired');
});
```

## Performance Considerations

### Database Indexing
```sql
-- Add indexes for faster queries
CREATE INDEX idx_conversation ON chat_messages(conversation_id);
CREATE INDEX idx_user_id ON chat_messages(user_id);
CREATE INDEX idx_created_at ON chat_messages(created_at);
```

### Caching Strategy
```java
@Cacheable("book_cache")
public List<Books> getAllBooks() { ... }

@CacheEvict("book_cache")
public void invalidateBookCache() { ... }
```

### Pagination for History
```java
// Get paginated history
public Page<ChatMessage> getConversationHistory(String convId, Pageable pageable)
```

## Security Considerations

### 1. Input Validation
- ✅ `@NotBlank` on prompt
- ✅ Max 2000 character limit
- ✅ SQL injection prevention via JPA

### 2. Authentication
- ✅ `@PreAuthorize("hasRole('USER')")`
- ✅ Security context propagation
- ✅ User ID extraction from token

### 3. Authorization
- ✅ Users can only access their conversations
- ✅ API key in environment variable

### 4. Rate Limiting
- ✅ Handled by Gemini API (429 errors)
- ⚠️ Consider adding backend rate limiting

## Troubleshooting

### Chatbot not responding

**Check:**
1. GEMINI_API_KEY is set
2. API key is valid
3. Database is running
4. User is logged in (has USER role)

### Conversation history not saving

**Check:**
1. `chat_messages` table exists
2. `spring.jpa.hibernate.ddl-auto` is not `create-drop`
3. Database connection is working
4. Logging shows no SQL errors

### RAG not providing context

**Check:**
1. Books exist in database
2. Search keywords match book names/authors/categories
3. Check RagService logs for search results
4. Verify Gemini prompt includes context

### Frontend errors

**Check:**
1. ApiService is injected in ChatbotService
2. Interceptors are properly configured
3. Conversation ID is being generated
4. Environment URL is correct

## Code Examples

### Add Custom RAG Context

```java
// In RagService.java
public String retrieveCustomContext(String userQuery) {
    // Get user's borrowing history
    List<Loan> userLoans = loanRepository.findByUserId(userId);
    
    // Get user's preferences
    String userPreferences = extractPreferences(userLoans);
    
    // Build custom context
    return "User borrowing history: " + userLoans +
           "Preferences: " + userPreferences;
}
```

### Extend Conversation Context

```java
// In ConversationService.java
public String buildEnrichedContext(String conversationId, Integer userId) {
    String conversationHistory = buildConversationContext(conversationId);
    List<Loan> userLoans = loanService.getUserLoans(userId);
    String userProfile = buildUserProfile(userLoans);
    
    return conversationHistory + "\n\n" + userProfile;
}
```

## Deployment Checklist

- [ ] ✅ GEMINI_API_KEY environment variable set
- [ ] ✅ Database migration includes chat_messages table
- [ ] ✅ Indexes created for performance
- [ ] ✅ ApiService is configured correctly
- [ ] ✅ ChatbotComponent imports OnDestroy
- [ ] ✅ RagService dependencies injected
- [ ] ✅ ConversationService bean configured
- [ ] ✅ Error interceptor registered
- [ ] ✅ CORS configured for chatbot endpoint
- [ ] ✅ Rate limiting configured (optional)

## Summary

The RAG chatbot is now production-ready with:
- ✅ Library-aware responses
- ✅ Conversation memory
- ✅ Error handling
- ✅ Database persistence
- ✅ Frontend integration
- ✅ Security features

**Next Steps:**
1. Test thoroughly in development
2. Deploy database migrations
3. Monitor Gemini API usage
4. Gather user feedback
5. Plan Phase 1 improvements (vector embeddings)

---

**For Questions:** See main documentation files in the project root directory.
