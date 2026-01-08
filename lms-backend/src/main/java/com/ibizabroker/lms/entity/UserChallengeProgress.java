package com.ibizabroker.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Feature 9: Gamification - Tiến độ thử thách của người dùng
 */
@Entity
@Table(name = "user_challenge_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "challenge_id"})
})
public class UserChallengeProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private ReadingChallenge challenge;

    @Column(name = "books_completed", nullable = false)
    private Integer booksCompleted = 0;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public ReadingChallenge getChallenge() { return challenge; }
    public void setChallenge(ReadingChallenge challenge) { this.challenge = challenge; }

    public Integer getBooksCompleted() { return booksCompleted; }
    public void setBooksCompleted(Integer booksCompleted) { this.booksCompleted = booksCompleted; }

    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    public void incrementProgress() {
        this.booksCompleted++;
        if (this.booksCompleted >= challenge.getTargetBooks() && !this.isCompleted) {
            this.isCompleted = true;
            this.completedAt = LocalDateTime.now();
        }
    }
}
