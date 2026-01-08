package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Integer> {
    
    // Lấy tất cả comments của một review, sắp xếp theo thời gian tạo
    List<ReviewComment> findByReview_IdOrderByCreatedAtAsc(Integer reviewId);
    
    // Đếm số comment của một review
    Long countByReview_Id(Integer reviewId);
}
