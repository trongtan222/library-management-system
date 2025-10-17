package com.ibizabroker.lms.dto;

import java.util.List;
import java.util.Map;

// DTO tổng hợp tất cả dữ liệu cho một báo cáo
public class ReportSummaryDto {
    private List<Map<String, Object>> loansByMonth;
    private List<Map<String, Object>> mostLoanedBooks;
    private List<Map<String, Object>> finesByMonth;

    // Getters and Setters
    public List<Map<String, Object>> getLoansByMonth() { return loansByMonth; }
    public void setLoansByMonth(List<Map<String, Object>> loansByMonth) { this.loansByMonth = loansByMonth; }

    public List<Map<String, Object>> getMostLoanedBooks() { return mostLoanedBooks; }
    public void setMostLoanedBooks(List<Map<String, Object>> mostLoanedBooks) { this.mostLoanedBooks = mostLoanedBooks; }

    public List<Map<String, Object>> getFinesByMonth() { return finesByMonth; }
    public void setFinesByMonth(List<Map<String, Object>> finesByMonth) { this.finesByMonth = finesByMonth; }
}
