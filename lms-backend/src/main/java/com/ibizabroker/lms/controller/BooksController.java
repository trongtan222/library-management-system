package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dto.BookCreateDto; // Import DTO
import com.ibizabroker.lms.dto.BookUpdateDto; // Import DTO
import com.ibizabroker.lms.entity.Author;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.entity.Category;
import com.ibizabroker.lms.service.BookService; // Import service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // Thêm
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/admin/books") // <--- THÊM /api
@PreAuthorize("hasRole('ADMIN')") // Bảo vệ tất cả các endpoint trong controller này
@RequiredArgsConstructor // Thêm
public class BooksController {

    private final BookService bookService; // Chỉ inject service

    // GET /admin/books đã bị xóa, vì admin sẽ dùng
    // GET /public/books?availableOnly=false (đã được phân trang)

    // API này giữ lại (hoặc chuyển sang PublicBooksController)
    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Integer id) {
        Books book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public Books createBook(@Valid @RequestBody BookCreateDto bookDto) { // Dùng DTO
        return bookService.createBook(bookDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable Integer id, @Valid @RequestBody BookUpdateDto bookDto) { // Dùng DTO
        Books updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(Map.of("deleted", Boolean.TRUE));
    }

    // --- API mới để hỗ trợ form ---
    
    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        return bookService.getAllAuthors();
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return bookService.getAllCategories();
    }
}