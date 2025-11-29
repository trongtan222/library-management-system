package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.entity.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagService {
    
    private final BooksRepository booksRepository;

    /**
     * Retrieve relevant books based on user query using simple keyword matching
     * In production, this would use vector embeddings and semantic search
     */
    @Transactional(readOnly = true)
    public String retrieveContext(String userQuery) {
        String queryLower = userQuery.toLowerCase();
        
        // Search for books matching keywords
        List<Books> books = booksRepository.findAll().stream()
            .filter(book -> 
                book.getName().toLowerCase().contains(queryLower) ||
                book.getAuthors().stream().anyMatch(a -> a.getName().toLowerCase().contains(queryLower)) ||
                book.getCategories().stream().anyMatch(c -> c.getName().toLowerCase().contains(queryLower))
            )
            .limit(5)
            .collect(Collectors.toList());
        
        // Format context for RAG
        if (books.isEmpty()) {
            return "No specific books found in library matching the query.";
        }
        
        StringBuilder context = new StringBuilder("Relevant books in our library:\n");
        for (Books book : books) {
            context.append("- ").append(book.getName())
                .append(" by ").append(book.getAuthors().stream()
                    .map(a -> a.getName())
                    .collect(Collectors.joining(", ")))
                .append(" (Available: ").append(book.getNumberOfCopiesAvailable()).append(" copies)\n");
        }
        
        return context.toString();
    }

    /**
     * Build augmented prompt with library context
     */
    public String buildAugmentedPrompt(String userQuery) {
        String context = retrieveContext(userQuery);
        
        return "You are a helpful library assistant for a Library Management System.\n" +
               "Provide helpful, concise answers about books and library services.\n" +
               "If asked about borrowing, mention users can borrow books through the system.\n" +
               "If asked about specific books, check the library inventory below.\n\n" +
               "=== LIBRARY CONTEXT ===\n" +
               context + "\n" +
               "=== USER QUESTION ===\n" +
               userQuery;
    }
}
