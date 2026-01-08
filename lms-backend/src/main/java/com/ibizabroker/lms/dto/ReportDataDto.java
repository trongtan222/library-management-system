package com.ibizabroker.lms.dto;

public class ReportDataDto {

    private Long totalLoans;
    private Long returnedLoans;
    private Long overdueLoans;
    private Double totalFines;

    public ReportDataDto() {}

    public ReportDataDto(Long totalLoans, Long returnedLoans, Long overdueLoans, Double totalFines) {
        this.totalLoans = totalLoans;
        this.returnedLoans = returnedLoans;
        this.overdueLoans = overdueLoans;
        this.totalFines = totalFines;
    }

    // Getters and Setters
    public Long getTotalLoans() { return totalLoans; }
    public void setTotalLoans(Long totalLoans) { this.totalLoans = totalLoans; }

    public Long getReturnedLoans() { return returnedLoans; }
    public void setReturnedLoans(Long returnedLoans) { this.returnedLoans = returnedLoans; }

    public Long getOverdueLoans() { return overdueLoans; }
    public void setOverdueLoans(Long overdueLoans) { this.overdueLoans = overdueLoans; }

    public Double getTotalFines() { return totalFines; }
    public void setTotalFines(Double totalFines) { this.totalFines = totalFines; }
}
