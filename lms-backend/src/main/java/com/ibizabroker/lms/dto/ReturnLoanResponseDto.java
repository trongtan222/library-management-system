package com.ibizabroker.lms.dto;

import com.ibizabroker.lms.entity.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReturnLoanResponseDto {
    private Integer loanId;
    private Integer bookId;
    private Integer memberId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private BigDecimal fineAmount;
    private Long overdueDays;

    public ReturnLoanResponseDto(Integer loanId,
                                 Integer bookId,
                                 Integer memberId,
                                 LocalDate loanDate,
                                 LocalDate dueDate,
                                 LocalDate returnDate,
                                 LoanStatus status,
                                 BigDecimal fineAmount,
                                 Long overdueDays) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fineAmount = fineAmount;
        this.overdueDays = overdueDays;
    }

    public Integer getLoanId() { return loanId; }
    public Integer getBookId() { return bookId; }
    public Integer getMemberId() { return memberId; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public LoanStatus getStatus() { return status; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public Long getOverdueDays() { return overdueDays; }
}
