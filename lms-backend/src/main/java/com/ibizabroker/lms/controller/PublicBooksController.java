package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/public/books")
@RequiredArgsConstructor
public class PublicBooksController {

    private final BooksRepository booksRepository;

    @GetMapping
    public Page<Books> listBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "true") boolean availableOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return booksRepository.findWithFiltersAndPagination(search, genre, availableOnly, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Integer id) {
        Books book = booksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found."));
        return ResponseEntity.ok(book);
    }

    @GetMapping("/newest")
    public List<Books> getNewestBooks() {
        return booksRepository.findTop10ByOrderByIdDesc();
    }
}