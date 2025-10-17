package com.ibizabroker.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineDetailsDto {
    private Integer loanId;
    private String bookName;
    private String userName;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Long overdueDays;
    private BigDecimal fineAmount;

    // Constructor để JPA Query sử dụng
    public FineDetailsDto(Integer loanId, String bookName, String userName, LocalDate dueDate, LocalDate returnDate, BigDecimal fineAmount) {
        this.loanId = loanId;
        this.bookName = bookName;
        this.userName = userName;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        if (dueDate != null && returnDate != null && returnDate.isAfter(dueDate)) {
            this.overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        } else {
            this.overdueDays = 0L;
        }
    }

    // === BỔ SUNG CÁC GETTER CÒN THIẾU TẠI ĐÂY ===

    public Integer getLoanId() {
        return loanId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Long getOverdueDays() {
        return overdueDays;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }
}