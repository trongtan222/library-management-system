package com.ibizabroker.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Feature 9: Gamification - Huy hiệu người dùng đã đạt được
 */
@Entity
@Table(name = "user_badges", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "badge_id"})
})
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @Column(name = "earned_at", nullable = false)
    private LocalDateTime earnedAt = LocalDateTime.now();

    @Column(name = "is_displayed", nullable = false)
    private Boolean isDisplayed = true; // Hiển thị trên profile

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Badge getBadge() { return badge; }
    public void setBadge(Badge badge) { this.badge = badge; }

    public LocalDateTime getEarnedAt() { return earnedAt; }
    public void setEarnedAt(LocalDateTime earnedAt) { this.earnedAt = earnedAt; }

    public Boolean getIsDisplayed() { return isDisplayed; }
    public void setIsDisplayed(Boolean isDisplayed) { this.isDisplayed = isDisplayed; }
}
