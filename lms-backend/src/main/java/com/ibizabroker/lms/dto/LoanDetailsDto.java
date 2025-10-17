package com.ibizabroker.lms.dto;

import com.ibizabroker.lms.entity.LoanStatus;
import java.time.LocalDate;

public class LoanDetailsDto {
    private Integer loanId;
    private String bookName;
    private String userName;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;

    // SỬA LỖI TẠI ĐÂY:
    // Constructor này nhận vào các giá trị riêng lẻ, khớp với câu truy vấn trong LoanRepository
    public LoanDetailsDto(Integer loanId, String bookName, String userName, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, LoanStatus status) {
        this.loanId = loanId;
        this.bookName = bookName;
        this.userName = userName;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters
    public Integer getLoanId() { return loanId; }
    public String getBookName() { return bookName; }
    public String getUserName() { return userName; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public LoanStatus getStatus() { return status; }
}