package com.ibizabroker.lms.dto;

public class RenewRequest {
    private Integer loanId;
    private Integer extraDays = 7;

    public Integer getLoanId() { return loanId; }
    public void setLoanId(Integer loanId) { this.loanId = loanId; }
    public Integer getExtraDays() { return extraDays; }
    public void setExtraDays(Integer extraDays) { this.extraDays = extraDays; }
}
