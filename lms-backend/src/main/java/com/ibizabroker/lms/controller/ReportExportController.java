package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dto.ReportDataDto;
import com.ibizabroker.lms.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Feature 3: Report Export Controller
 */
@RestController
@RequestMapping("/api/admin/reports/export")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportExportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<ReportDataDto> getReportSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getReportSummary(startDate, endDate));
    }

    @GetMapping("/loans/excel")
    public ResponseEntity<byte[]> exportLoansExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws IOException {
        
        byte[] excelBytes = reportService.generateLoansExcelReport(startDate, endDate);
        String filename = "bao_cao_muon_sach_" + startDate + "_" + endDate + ".xlsx";
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(excelBytes);
    }

    @GetMapping("/books/excel")
    public ResponseEntity<byte[]> exportBooksExcel() throws IOException {
        byte[] excelBytes = reportService.generateBooksExcelReport();
        String filename = "danh_sach_sach_" + LocalDate.now() + ".xlsx";
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(excelBytes);
    }

    @GetMapping("/users/excel")
    public ResponseEntity<byte[]> exportUsersExcel() throws IOException {
        byte[] excelBytes = reportService.generateUsersExcelReport();
        String filename = "danh_sach_nguoi_dung_" + LocalDate.now() + ".xlsx";
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(excelBytes);
    }
}
