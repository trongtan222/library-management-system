// lms-backend/src/main/java/com/ibizabroker/lms/controller/ReportController.java

package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.LoanRepository;
import com.ibizabroker.lms.dto.ReportSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final LoanRepository loanRepository;

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryDto> getReportSummary(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        ReportSummaryDto summary = new ReportSummaryDto();
        
        // Gọi các phương thức từ repository và gán vào DTO
        summary.setLoansByMonth(loanRepository.findLoanCountsByMonth(start, end));
        summary.setMostLoanedBooks(loanRepository.findMostLoanedBooksInPeriod(start, end));
        // Bạn có thể thêm các thống kê khác ở đây, ví dụ: finesByMonth
        
        return ResponseEntity.ok(summary);
    }
}