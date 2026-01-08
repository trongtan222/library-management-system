package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.entity.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Vector sync controller - currently deprecated as we're using simple database search instead of vector embeddings.
 * This endpoint can be used for future enhancement when vector search is implemented.
 */
@RestController
@RequestMapping("/api/admin/vector")
@RequiredArgsConstructor
public class VectorSyncController {

    private final BooksRepository booksRepository;

    @PostMapping("/sync")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> syncAllBooksToVectorStore() {
        List<Books> allBooks = booksRepository.findAll();
        
        // Currently we use database text search instead of vector embeddings
        // When vector search is implemented, this endpoint can sync books to Pinecone/similar service
        
        return ResponseEntity.ok("Vector sync currently disabled. Using database search instead. Total books: " + allBooks.size());
    }
}
