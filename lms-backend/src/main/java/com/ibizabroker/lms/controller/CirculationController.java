package com.ibizabroker.lms.controller;

import com.ibizabroker.lms.dao.LoanRepository;
import com.ibizabroker.lms.dao.UsersRepository;
import com.ibizabroker.lms.dto.FineDetailsDto;
import com.ibizabroker.lms.dto.LoanDetailsDto;
import com.ibizabroker.lms.dto.LoanRequest;
import com.ibizabroker.lms.dto.RenewRequest;
import com.ibizabroker.lms.dto.ReservationRequest;
import com.ibizabroker.lms.entity.Loan;
import com.ibizabroker.lms.entity.Reservation;
import com.ibizabroker.lms.entity.Users;
import com.ibizabroker.lms.exceptions.NotFoundException;
import com.ibizabroker.lms.service.CirculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/circulation") // <--- THÊM /api VÀO ĐÂY
@CrossOrigin("http://localhost:4200/")
@RequiredArgsConstructor
public class CirculationController {

    private final CirculationService service;
    // Bổ sung các repository cần thiết
    private final LoanRepository loanRepository;
    private final UsersRepository usersRepository;

    // ---------- LOAN CRUD ----------
    @PostMapping("/loans")
    public ResponseEntity<Loan> loan(@RequestBody LoanRequest req) {
        return ResponseEntity.ok(service.loanBook(req));
    }

    @PutMapping("/loans/{id}/return")
    public ResponseEntity<Loan> returnLoan(@PathVariable Integer id) {
        return ResponseEntity.ok(service.returnBook(id));
    }

    @PutMapping("/loans/renew")
    public ResponseEntity<Loan> renew(@RequestBody RenewRequest req) {
        return ResponseEntity.ok(service.renewLoan(req));
    }

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> listLoans(
            @RequestParam(required = false) Integer memberId,
            @RequestParam(required = false) Integer bookId,
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        List<Loan> out = service.findLoans(memberId, bookId, activeOnly);
        return ResponseEntity.ok(out);
    }
    
    // === TỐI ƯU HÓA: ENDPOINT MỚI CHO "MY ACCOUNT" ===
    @GetMapping("/my-loans")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LoanDetailsDto>> getMyLoanHistory(@AuthenticationPrincipal UserDetails userDetails) {
        Users currentUser = usersRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found from token"));
        
        List<LoanDetailsDto> loans = loanRepository.findLoanDetailsByMemberId(currentUser.getUserId());
        return ResponseEntity.ok(loans);
    }

    // ---------- RESERVATION ----------
    @PostMapping("/reservations")
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequest req) {
        return ResponseEntity.ok(service.placeReservation(req));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Integer id) {
        service.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> listReservations(@RequestParam Integer memberId) {
        List<Reservation> reservations = service.findReservations(memberId);
        return ResponseEntity.ok(reservations);
    }

    // ---------- FINES (API an toàn cho người dùng) ----------
    @GetMapping("/my-fines")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FineDetailsDto>> getMyFines(@AuthenticationPrincipal UserDetails userDetails) {
        // Lấy thông tin người dùng từ token, không tin tưởng ID từ client
        Users currentUser = usersRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found for token"));

        // Lấy danh sách phí phạt dựa trên ID người dùng đã được xác thực
        List<FineDetailsDto> fines = loanRepository.findUnpaidFineDetailsByMemberId(currentUser.getUserId());
        return ResponseEntity.ok(fines);
    }
}