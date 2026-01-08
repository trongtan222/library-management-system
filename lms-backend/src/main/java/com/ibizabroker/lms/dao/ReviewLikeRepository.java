package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Integer> {
    
    // Kiểm tra user đã like review chưa
    boolean existsByReview_IdAndUser_UserId(Integer reviewId, Integer userId);
    
    // Tìm like của user cho review cụ thể
    Optional<ReviewLike> findByReview_IdAndUser_UserId(Integer reviewId, Integer userId);
    
    // Đếm số like của một review
    @Query("SELECT COUNT(l) FROM ReviewLike l WHERE l.review.id = :reviewId")
    Long countByReviewId(@Param("reviewId") Integer reviewId);
}
