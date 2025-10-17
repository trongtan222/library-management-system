package com.ibizabroker.lms.dto;

public class DashboardStatsDto {
    private long totalBooks;
    private long totalUsers;
    private long activeLoans;
    private long overdueLoans;

    // Constructors
    public DashboardStatsDto() {}

    public DashboardStatsDto(long totalBooks, long totalUsers, long activeLoans, long overdueLoans) {
        this.totalBooks = totalBooks;
        this.totalUsers = totalUsers;
        this.activeLoans = activeLoans;
        this.overdueLoans = overdueLoans;
    }

    // Getters and Setters
    public long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getActiveLoans() { return activeLoans; }
    public void setActiveLoans(long activeLoans) { this.activeLoans = activeLoans; }

    public long getOverdueLoans() { return overdueLoans; }
    public void setOverdueLoans(long overdueLoans) { this.overdueLoans = overdueLoans; }
}