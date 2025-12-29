package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.RenewalRequest;
import com.ibizabroker.lms.entity.RenewalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RenewalRequestRepository extends JpaRepository<RenewalRequest, Long> {
    List<RenewalRequest> findByStatus(RenewalStatus status);
    List<RenewalRequest> findByMemberId(Integer memberId);
    RenewalRequest findFirstByLoanIdAndStatus(Integer loanId, RenewalStatus status);
}
