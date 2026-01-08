package com.ibizabroker.lms.dto;

public class GamificationStatsDto {

    private Integer totalPoints;
    private Integer currentLevel;
    private Integer rank;
    private Integer badgesCount;
    private Integer booksBorrowedCount;
    private Integer booksReturnedOnTime;
    private Integer reviewsWritten;
    private Integer streakDays;
    private Long activeChallenges;
    private Long completedChallenges;

    public GamificationStatsDto() {}

    public GamificationStatsDto(Integer totalPoints, Integer currentLevel, Integer rank,
                                 Integer badgesCount, Integer booksBorrowedCount, Integer booksReturnedOnTime,
                                 Integer reviewsWritten, Integer streakDays, Long activeChallenges, Long completedChallenges) {
        this.totalPoints = totalPoints;
        this.currentLevel = currentLevel;
        this.rank = rank;
        this.badgesCount = badgesCount;
        this.booksBorrowedCount = booksBorrowedCount;
        this.booksReturnedOnTime = booksReturnedOnTime;
        this.reviewsWritten = reviewsWritten;
        this.streakDays = streakDays;
        this.activeChallenges = activeChallenges;
        this.completedChallenges = completedChallenges;
    }

    // Getters and Setters
    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public Integer getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Integer currentLevel) { this.currentLevel = currentLevel; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public Integer getBadgesCount() { return badgesCount; }
    public void setBadgesCount(Integer badgesCount) { this.badgesCount = badgesCount; }

    public Integer getBooksBorrowedCount() { return booksBorrowedCount; }
    public void setBooksBorrowedCount(Integer booksBorrowedCount) { this.booksBorrowedCount = booksBorrowedCount; }

    public Integer getBooksReturnedOnTime() { return booksReturnedOnTime; }
    public void setBooksReturnedOnTime(Integer booksReturnedOnTime) { this.booksReturnedOnTime = booksReturnedOnTime; }

    public Integer getReviewsWritten() { return reviewsWritten; }
    public void setReviewsWritten(Integer reviewsWritten) { this.reviewsWritten = reviewsWritten; }

    public Integer getStreakDays() { return streakDays; }
    public void setStreakDays(Integer streakDays) { this.streakDays = streakDays; }

    public Long getActiveChallenges() { return activeChallenges; }
    public void setActiveChallenges(Long activeChallenges) { this.activeChallenges = activeChallenges; }

    public Long getCompletedChallenges() { return completedChallenges; }
    public void setCompletedChallenges(Long completedChallenges) { this.completedChallenges = completedChallenges; }
}
