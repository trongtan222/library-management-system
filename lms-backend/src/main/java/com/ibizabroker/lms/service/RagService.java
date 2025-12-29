package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.entity.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        // Limit to top 5 books to keep token size small
        Pageable topFive = PageRequest.of(0, 5);

        // Use repository query to avoid N+1 and pull only needed rows
        List<Books> books = booksRepository.findWithFiltersAndPagination(queryLower, null, false, topFive)
            .getContent();
        
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

        return new StringBuilder()
            .append("You are a helpful library assistant for a Library Management System.\n")
            .append("Provide helpful, concise answers about books and library services.\n")
            .append("If asked about borrowing, mention users can borrow books through the system.\n")
            .append("If asked about specific books, check the library inventory below.\n\n")
            .append("=== LIBRARY CONTEXT ===\n")
            .append(context)
            .append("\n=== USER QUESTION ===\n")
            .append(userQuery)
            .append("\n\nPlease answer the user's question in Vietnamese.")
            .toString();
    }
}
