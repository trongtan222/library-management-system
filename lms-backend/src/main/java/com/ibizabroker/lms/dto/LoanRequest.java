package com.ibizabroker.lms.dto;

public class LoanRequest {
    private Integer bookId;
    private Integer memberId;
    private Integer loanDays = 14;

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getLoanDays() { return loanDays; }
    public void setLoanDays(Integer loanDays) { this.loanDays = loanDays; }
}
