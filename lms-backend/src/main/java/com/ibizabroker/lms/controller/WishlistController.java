package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "http://localhost:4200") // Cho phép Angular gọi
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // API: POST /api/wishlist/add/{bookId}
    @PostMapping("/add/{bookId}")
    public ResponseEntity<?> addToWishlist(@PathVariable Long bookId) {
        wishlistService.addToWishlist(bookId);
        return ResponseEntity.ok(Map.of("message", "Đã thêm vào yêu thích"));
    }

    // API: DELETE /api/wishlist/remove/{bookId}
    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long bookId) {
        wishlistService.removeFromWishlist(bookId);
        return ResponseEntity.ok(Map.of("message", "Đã xóa khỏi yêu thích"));
    }

    // API: GET /api/wishlist/check/{bookId}
    @GetMapping("/check/{bookId}")
    public ResponseEntity<Boolean> checkStatus(@PathVariable Long bookId) {
        return ResponseEntity.ok(wishlistService.isWishlisted(bookId));
    }
    
    // API: GET /api/wishlist/my-wishlist (Để hiển thị danh sách nếu cần)
    @GetMapping("/my-wishlist")
    public ResponseEntity<?> getMyWishlist() {
        return ResponseEntity.ok(wishlistService.getMyWishlist());
    }
}