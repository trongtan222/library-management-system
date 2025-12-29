package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dao.WishlistRepository;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.entity.Wishlist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@Tag(name = "Wishlist")
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final UsersRepository usersRepository;
    private final BooksRepository booksRepository;

    public WishlistController(WishlistRepository wishlistRepository,
                              UsersRepository usersRepository,
                              BooksRepository booksRepository) {
        this.wishlistRepository = wishlistRepository;
        this.usersRepository = usersRepository;
        this.booksRepository = booksRepository;
    }

    @GetMapping(value = "/my-wishlist", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get current user's wishlist")
    public ResponseEntity<?> getMyWishlist() {
        Users user = currentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status","error","error","Unauthorized"));
        List<Wishlist> entries = wishlistRepository.findByUser(user);
        // return a list of books for simplicity
        List<Books> books = entries.stream().map(Wishlist::getBook).collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    @PostMapping(value = "/add/{bookId}")
    @Operation(summary = "Add a book to wishlist")
    public ResponseEntity<?> add(@PathVariable Integer bookId) {
        Users user = currentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status","error","error","Unauthorized"));
        Books book = booksRepository.findById(bookId).orElse(null);
        if (book == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status","error","error","Book not found"));
        if (!wishlistRepository.existsByUserAndBook(user, book)) {
            Wishlist w = new Wishlist();
            w.setUser(user);
            w.setBook(book);
            wishlistRepository.save(w);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status","ok"));
    }

    @DeleteMapping(value = "/remove/{bookId}")
    @Operation(summary = "Remove a book from wishlist")
    public ResponseEntity<?> remove(@PathVariable Integer bookId) {
        Users user = currentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status","error","error","Unauthorized"));
        Books book = booksRepository.findById(bookId).orElse(null);
        if (book == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status","error","error","Book not found"));
        wishlistRepository.deleteByUserAndBook(user, book);
        return ResponseEntity.ok(Map.of("status","ok"));
    }

    @GetMapping(value = "/check/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Check if a book is in wishlist")
    public ResponseEntity<Boolean> check(@PathVariable Integer bookId) {
        Users user = currentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Books book = booksRepository.findById(bookId).orElse(null);
        if (book == null) return ResponseEntity.ok(false);
        boolean exists = wishlistRepository.existsByUserAndBook(user, book);
        return ResponseEntity.ok(exists);
    }

    private Users currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) return null;
        return usersRepository.findByUsername(auth.getName()).orElse(null);
    }
}
