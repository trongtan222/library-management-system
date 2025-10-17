package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin/books") // Đã có tiền tố /admin
public class BooksController {

    @Autowired
    private BooksRepository booksRepository;

    @GetMapping
    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Integer id) {
        Books book = booksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " does not exist."));
        return ResponseEntity.ok(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Books createBook(@RequestBody Books book) {
        book.setId(null);
        return booksRepository.save(book);
    }
// ... (các phương thức còn lại không thay đổi)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable Integer id, @RequestBody Books req) {
        // Sửa ở đây
        Books book = booksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " does not exist."));

        if (req.getName() != null) book.setName(req.getName());
        if (req.getAuthor() != null) book.setAuthor(req.getAuthor());
        if (req.getGenre() != null) book.setGenre(req.getGenre());
        if (req.getNumberOfCopiesAvailable() != null)
            book.setNumberOfCopiesAvailable(req.getNumberOfCopiesAvailable());
        if (req.getPublishedYear() != null) book.setPublishedYear(req.getPublishedYear());
        if (req.getIsbn() != null) book.setIsbn(req.getIsbn());
        
        // Đảm bảo logic lưu coverUrl hoạt động
        if (req.getCoverUrl() != null) {
            book.setCoverUrl(req.getCoverUrl());
        }

        return ResponseEntity.ok(booksRepository.save(book));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBook(@PathVariable Integer id) {
        // Sửa ở đây
        Books book = booksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " does not exist."));
        booksRepository.delete(book);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
