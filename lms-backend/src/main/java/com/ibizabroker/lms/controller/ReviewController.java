package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dao.ReviewRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dto.ReviewDto;
import com.ibizabroker.lms.entity.Books;
import com.ibizabroker.lms.entity.Review;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final BooksRepository booksRepository;
    private final UsersRepository usersRepository;

    public ReviewController(ReviewRepository reviewRepository, BooksRepository booksRepository, UsersRepository usersRepository) {
        this.reviewRepository = reviewRepository;
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
    }

    // --- API CHO NGƯỜI DÙNG ---

    @PostMapping("/api/books/{bookId}/reviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addReview(@PathVariable Integer bookId,
                                       @Valid @RequestBody ReviewDto reviewDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        Users currentUser = usersRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Người dùng không hợp lệ"));

        if (reviewRepository.existsByBookIdAndUser_UserId(bookId, currentUser.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Bạn đã đánh giá sách này rồi."));
        }

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sách với ID: " + bookId));

        Review review = new Review();
        review.setBook(book);
        review.setUser(currentUser);
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());

        Review savedReview = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(savedReview));
    }

    @GetMapping("/api/books/{bookId}/reviews")
    public ResponseEntity<Map<String, Object>> getReviewsForBook(@PathVariable Integer bookId) {
        List<ReviewDto> reviews = reviewRepository.findByBookIdAndApproved(bookId, true)
                .stream().map(this::toDto).collect(Collectors.toList());
        
        Double averageRating = reviewRepository.findAverageRatingByBookId(bookId).orElse(0.0);

        return ResponseEntity.ok(Map.of(
            "reviews", reviews,
            "averageRating", averageRating
        ));
    }

    // --- API CHO ADMIN ---

    @GetMapping("/admin/reviews") // <-- Đảm bảo endpoint này tồn tại
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/books/{bookId}/reviews/check")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Boolean>> hasUserReviewed(
            @PathVariable Integer bookId, 
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Users currentUser = usersRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Người dùng không hợp lệ"));
            
        boolean hasReviewed = reviewRepository.existsByBookIdAndUser_UserId(bookId, currentUser.getUserId());
    
        return ResponseEntity.ok(Map.of("hasReviewed", hasReviewed));
    }


    @PutMapping("/admin/reviews/{reviewId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewDto> approveReview(@PathVariable Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đánh giá với ID: " + reviewId));
        
        review.setApproved(true);
        Review updatedReview = reviewRepository.save(review);
        return ResponseEntity.ok(toDto(updatedReview));
    }
    
    // --- HÀM TIỆN ÍCH ---

    private ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.id = review.getId();
        dto.bookId = review.getBook().getId();
        dto.bookName = review.getBook().getName();
        dto.userId = review.getUser().getUserId();
        dto.userName = review.getUser().getName();
        dto.rating = review.getRating();
        dto.comment = review.getComment();
        dto.approved = review.isApproved();
        dto.createdAt = review.getCreatedAt().toString();
        return dto;
    }
}
