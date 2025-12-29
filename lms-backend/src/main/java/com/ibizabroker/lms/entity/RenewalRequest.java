package com.ibizabroker.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "renewal_requests")
public class RenewalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer loanId;

    @Column(nullable = false)
    private Integer memberId;

    @Column(nullable = false)
    private Integer extraDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RenewalStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime decidedAt;

    private String adminNote;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getLoanId() { return loanId; }
    public void setLoanId(Integer loanId) { this.loanId = loanId; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getExtraDays() { return extraDays; }
    public void setExtraDays(Integer extraDays) { this.extraDays = extraDays; }
    public RenewalStatus getStatus() { return status; }
    public void setStatus(RenewalStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getDecidedAt() { return decidedAt; }
    public void setDecidedAt(LocalDateTime decidedAt) { this.decidedAt = decidedAt; }
    public String getAdminNote() { return adminNote; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }
}
