package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.entity.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagIndexer {

    private final BooksRepository booksRepository;
    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    @Transactional(readOnly = true)
    public void reindexAll() {
        List<Books> books = booksRepository.findAll();
        for (Books b : books) {
            indexBook(b);
        }
    }

    public void indexBook(Books book) {
        if (book == null) return;
        String text = buildBookText(book);
        var vector = embeddingService.embed(text);
        if (vector.isEmpty()) return;
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", book.getName());
        metadata.put("isbn", book.getIsbn());
        vectorStore.upsert(String.valueOf(book.getId()), vector, metadata);
    }

    private String buildBookText(Books book) {
        String authors = book.getAuthors() == null ? "" : book.getAuthors().stream()
                .map(a -> a.getName())
                .collect(Collectors.joining(", "));
        String categories = book.getCategories() == null ? "" : book.getCategories().stream()
                .map(c -> c.getName())
                .collect(Collectors.joining(", "));
        return String.join(" \n ",
                safe(book.getName()),
                "Authors: " + authors,
                "Categories: " + categories,
                "ISBN: " + safe(book.getIsbn())
        );
    }

    private String safe(String v) {
        return v == null ? "" : v;
    }
}
