package com.ibizabroker.lms.dto;

import com.ibizabroker.lms.entity.Loan;

import java.util.List;
import java.util.Map;

public class DashboardDetailsDto {
    private DashboardStatsDto stats;
    private List<Map<String, Object>> mostLoanedBooks;
    private List<Map<String, Object>> topBorrowers;
    private List<Loan> recentActivities;
    private List<Loan> overdueLoans;

    // Getters and Setters
    public DashboardStatsDto getStats() { return stats; }
    public void setStats(DashboardStatsDto stats) { this.stats = stats; }

    public List<Map<String, Object>> getMostLoanedBooks() { return mostLoanedBooks; }
    public void setMostLoanedBooks(List<Map<String, Object>> mostLoanedBooks) { this.mostLoanedBooks = mostLoanedBooks; }

    public List<Map<String, Object>> getTopBorrowers() { return topBorrowers; }
    public void setTopBorrowers(List<Map<String, Object>> topBorrowers) { this.topBorrowers = topBorrowers; }

    public List<Loan> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<Loan> recentActivities) { this.recentActivities = recentActivities; }
    
    public List<Loan> getOverdueLoans() { return overdueLoans; }
    public void setOverdueLoans(List<Loan> overdueLoans) { this.overdueLoans = overdueLoans; }
}
