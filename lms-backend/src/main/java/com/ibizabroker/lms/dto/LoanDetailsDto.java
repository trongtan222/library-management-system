package com.ibizabroker.lms.dto;

import com.ibizabroker.lms.entity.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanDetailsDto {
    private Integer loanId;
    private String bookName;
    private String userName;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;

    // thông tin phạt
    private BigDecimal fineAmount;
    private Long overdueDays;

    // Constructor khớp với câu truy vấn trong LoanRepository
    public LoanDetailsDto(Integer loanId,
                          String bookName,
                          String userName,
                          LocalDate loanDate,
                          LocalDate dueDate,
                          LocalDate returnDate,
                          LoanStatus status,
                          BigDecimal fineAmount,
                          Long overdueDays) {
        this.loanId = loanId;
        this.bookName = bookName;
        this.userName = userName;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fineAmount = fineAmount;
        this.overdueDays = overdueDays;
    }

    // Getters
    public Integer getLoanId() { return loanId; }
    public String getBookName() { return bookName; }
    public String getUserName() { return userName; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public LoanStatus getStatus() { return status; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public Long getOverdueDays() { return overdueDays; }
}