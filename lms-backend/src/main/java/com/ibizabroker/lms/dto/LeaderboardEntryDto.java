package com.ibizabroker.lms.dto;

public class LeaderboardEntryDto {

    private Integer userId;
    private String userName;
    private Integer totalPoints;
    private Integer level;
    private Integer badgesCount;

    public LeaderboardEntryDto() {}

    public LeaderboardEntryDto(Integer userId, String userName, Integer totalPoints, Integer level, Integer badgesCount) {
        this.userId = userId;
        this.userName = userName;
        this.totalPoints = totalPoints;
        this.level = level;
        this.badgesCount = badgesCount;
    }

    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getBadgesCount() { return badgesCount; }
    public void setBadgesCount(Integer badgesCount) { this.badgesCount = badgesCount; }
}
