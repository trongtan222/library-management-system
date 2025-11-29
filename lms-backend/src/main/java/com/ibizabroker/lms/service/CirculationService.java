package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.BooksRepository;
import com.ibizabroker.lms.dao.LoanRepository;
import com.ibizabroker.lms.dao.ReservationRepository;
import com.ibizabroker.lms.dto.LoanRequest;
import com.ibizabroker.lms.dto.RenewRequest;
import com.ibizabroker.lms.dto.ReservationRequest;
import com.ibizabroker.lms.entity.*;
import com.ibizabroker.lms.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
public class CirculationService {

    private final BooksRepository booksRepo;
    private final LoanRepository loanRepo;
    private final ReservationRepository reservationRepo;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("2000"); // 2,000 VND

    public CirculationService(BooksRepository b, LoanRepository l, ReservationRepository r){
        this.booksRepo=b; this.loanRepo=l; this.reservationRepo=r;
    }

    @Transactional
    public Loan loanBook(LoanRequest req) {
        // 1. Xác định số lượng cần mượn (mặc định là 1 nếu không nhập)
        int quantityToBorrow = (req.getQuantity() != null && req.getQuantity() > 0) ? req.getQuantity() : 1;
        Loan lastLoan = null;

        // 2. Thực hiện vòng lặp để mượn từng cuốn
        for (int i = 0; i < quantityToBorrow; i++) {
            List<Reservation> queue = reservationRepo
                .findTop10ByBookIdAndStatusOrderByCreatedAtAsc(req.getBookId(), ReservationStatus.ACTIVE);
            
            if(!queue.isEmpty() && !queue.get(0).getMemberId().equals(req.getMemberId()))
                throw new IllegalStateException("Sách đang được giữ chỗ cho người khác.");

            // Trừ tồn kho
            int updated = booksRepo.decrementAvailable(req.getBookId());
            if(updated == 0) throw new IllegalStateException("Không đủ số lượng sách trong kho.");

            LocalDate today = LocalDate.now();
            Loan loan = new Loan();
            loan.setBookId(req.getBookId());
            loan.setMemberId(req.getMemberId());
            loan.setLoanDate(today);
            loan.setDueDate(today.plusDays(req.getLoanDays() != null ? req.getLoanDays() : 14));
            loan.setStatus(LoanStatus.ACTIVE);
            loan.setFineStatus("NO_FINE");
            
            // Lưu ý: Tạm thời chúng ta chưa lưu studentName/Class vào DB để tránh lỗi cấu trúc bảng.
            // Thông tin này hiện tại dùng để xác nhận ở phía Frontend.
            
            lastLoan = loanRepo.save(loan);

            if(!queue.isEmpty() && queue.get(0).getMemberId().equals(req.getMemberId())){
                Reservation r = queue.get(0);
                r.setStatus(ReservationStatus.FULFILLED);
                reservationRepo.save(r);
            }
        }
        
        return lastLoan; // Trả về đơn cuối cùng để lấy trạng thái
    }

    @Transactional
    public Loan returnBook(Integer loanId){
        Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new NotFoundException("Loan not found"));
        if(loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.OVERDUE) return loan;
        
        LocalDate returnDate = LocalDate.now();
        loan.setReturnDate(returnDate);
        loan.setStatus(LoanStatus.RETURNED);

        if (returnDate.isAfter(loan.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
            if (overdueDays > 0) {
                BigDecimal totalFine = FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));
                loan.setFineAmount(totalFine);
                loan.setFineStatus("UNPAID");
            }
        }
        
        booksRepo.incrementAvailable(loan.getBookId());
        return loanRepo.save(loan);
    }

    @Transactional
    public Loan renewLoan(RenewRequest req){
        Loan loan = loanRepo.findById(req.getLoanId()).orElseThrow(() -> new NotFoundException("Loan not found"));
        if(loan.getStatus() != LoanStatus.ACTIVE) throw new IllegalStateException("Loan is not active.");
        
        boolean isReservedByOthers = reservationRepo.existsByBookIdAndStatusAndMemberIdNot(
            loan.getBookId(), ReservationStatus.ACTIVE, loan.getMemberId());

        if(isReservedByOthers) throw new IllegalStateException("Book is reserved by another member; cannot renew.");
        
        int extra = req.getExtraDays()!=null?req.getExtraDays():7;
        loan.setDueDate(loan.getDueDate().plusDays(extra));
        return loanRepo.save(loan);
    }

    @Transactional
    public Reservation placeReservation(ReservationRequest req){
        if(reservationRepo.existsByBookIdAndMemberIdAndStatus(req.getBookId(), req.getMemberId(), ReservationStatus.ACTIVE))
            throw new IllegalStateException("Already reserved.");
        Reservation r = new Reservation();
        r.setBookId(req.getBookId());
        r.setMemberId(req.getMemberId());
        r.setStatus(ReservationStatus.ACTIVE);
        r.setCreatedAt(LocalDateTime.now());
        r.setExpireAt(LocalDateTime.now().plusDays(3));
        return reservationRepo.save(r);
    }

    @Transactional
    public void cancelReservation(Integer reservationId){
        Reservation r = reservationRepo.findById(reservationId).orElseThrow(() -> new NotFoundException("Reservation not found"));
        r.setStatus(ReservationStatus.CANCELLED);
        reservationRepo.save(r);
    }

    @Transactional(readOnly = true)
    public List<Loan> findLoans(Integer memberId, Integer bookId, boolean activeOnly) {
        if (memberId != null) {
            if (activeOnly) {
                return loanRepo.findByMemberIdAndStatus(memberId, LoanStatus.ACTIVE);
            } else {
                return loanRepo.findByMemberId(memberId);
            }
        }
        if (bookId != null) {
            return loanRepo.findByBookId(bookId);
        }
        throw new IllegalStateException("Yêu cầu phải có memberId hoặc bookId");
    }

    // === TRIỂN KHAI PHƯƠNG THỨC CÒN THIẾU TẠI ĐÂY ===
    @Transactional(readOnly = true)
    public List<Reservation> findReservations(Integer memberId) {
        return reservationRepo.findByMemberIdAndStatus(memberId, ReservationStatus.ACTIVE);
    }
}