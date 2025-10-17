package com.ibizabroker.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", uniqueConstraints = {
    // Đảm bảo mỗi người dùng chỉ đánh giá một cuốn sách một lần
    @UniqueConstraint(columnNames = {"book_id", "user_id"})
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotNull
    @Min(value = 1, message = "Điểm số phải từ 1 đến 5")
    @Max(value = 5, message = "Điểm số phải từ 1 đến 5")
    @Column(nullable = false)
    private Integer rating; // Điểm số từ 1-5

    @Column(columnDefinition = "TEXT")
    private String comment; // Bình luận (tùy chọn)

    @Column(nullable = false)
    private boolean approved = false; // Trạng thái phê duyệt, mặc định là false

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Books getBook() { return book; }
    public void setBook(Books book) { this.book = book; }
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}