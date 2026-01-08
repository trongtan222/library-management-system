package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.exceptions.NotFoundException;
import com.ibizabroker.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/books")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class PublicBooksController {

    private final BookService bookService;

    @GetMapping
    public Page<Books> listBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean availableOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.findBooksWithFilters(search, genre, availableOnly, pageable);
    }

    @GetMapping("/newest")
    public ResponseEntity<List<Books>> getNewestBooks() {
        Pageable top10 = PageRequest.of(0, 10);
        return ResponseEntity.ok(bookService.getNewestBooks(top10));
    }

    // ✅ BỔ SUNG API NÀY ĐỂ TÍNH NĂNG QUÉT QR HOẠT ĐỘNG
    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(bookService.getBookById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}