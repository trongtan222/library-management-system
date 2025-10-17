package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.LoanRepository;
import com.ibizabroker.lms.dto.LoanDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/loans")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminLoanController {

    private final LoanRepository loanRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanDetailsDto>> getAllLoans() {
        // SỬA LỖI TẠI ĐÂY:
        // Gọi trực tiếp phương thức findAllLoanDetails() mà chúng ta đã tạo trong repository.
        // Phương thức này sẽ trả về chính xác danh sách List<LoanDetailsDto> mà chúng ta cần.
        List<LoanDetailsDto> loans = loanRepository.findAllLoanDetails();
        return ResponseEntity.ok(loans);
    }
}