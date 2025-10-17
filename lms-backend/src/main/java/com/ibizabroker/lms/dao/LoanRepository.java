package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.dto.FineDetailsDto;
import com.ibizabroker.lms.dto.LoanDetailsDto;
import com.ibizabroker.lms.entity.Loan;
import com.ibizabroker.lms.entity.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    // ... (các phương thức hiện có)
    Page<Loan> findByMemberId(Integer memberId, Pageable pageable);
    List<Loan> findByMemberId(Integer memberId);
    List<Loan> findByMemberIdAndStatus(Integer memberId, LoanStatus status);
    List<Loan> findByBookId(Integer bookId);
    long countByStatus(LoanStatus status);
    List<Loan> findTop5ByOrderByLoanDateDesc();
    List<Loan> findByStatus(LoanStatus status);

    @Query("SELECT new com.ibizabroker.lms.dto.LoanDetailsDto(l.id, b.name, u.name, l.loanDate, l.dueDate, l.returnDate, l.status) " +
           "FROM Loan l JOIN Books b ON l.bookId = b.id JOIN Users u ON l.memberId = u.userId " +
           "ORDER BY l.loanDate DESC")
    List<LoanDetailsDto> findAllLoanDetails();

    // === BỔ SUNG PHƯƠNG THỨC MỚI TẠI ĐÂY ===
    @Query("SELECT new com.ibizabroker.lms.dto.LoanDetailsDto(l.id, b.name, u.name, l.loanDate, l.dueDate, l.returnDate, l.status) " +
           "FROM Loan l JOIN Books b ON l.bookId = b.id JOIN Users u ON l.memberId = u.userId " +
           "WHERE l.memberId = :memberId ORDER BY l.loanDate DESC")
    List<LoanDetailsDto> findLoanDetailsByMemberId(@Param("memberId") Integer memberId);


    @Query("SELECT l.bookId as bookId, COUNT(l.bookId) as loanCount FROM Loan l GROUP BY l.bookId ORDER BY loanCount DESC")
    List<Map<String, Object>> findMostLoanedBooks(Pageable pageable);

    @Query("SELECT l.memberId as memberId, COUNT(l.memberId) as loanCount FROM Loan l GROUP BY l.memberId ORDER BY loanCount DESC")
    List<Map<String, Object>> findTopBorrowers(Pageable pageable);

    @Query("SELECT new com.ibizabroker.lms.dto.FineDetailsDto(l.id, b.name, u.name, l.dueDate, l.returnDate, l.fineAmount) " +
           "FROM Loan l JOIN Books b ON l.bookId = b.id JOIN Users u ON l.memberId = u.userId " +
           "WHERE l.fineStatus = 'UNPAID' ORDER BY l.returnDate DESC")
    List<FineDetailsDto> findUnpaidFineDetails();

    @Query("SELECT new com.ibizabroker.lms.dto.FineDetailsDto(l.id, b.name, u.name, l.dueDate, l.returnDate, l.fineAmount) " +
           "FROM Loan l JOIN Books b ON l.bookId = b.id JOIN Users u ON l.memberId = u.userId " +
           "WHERE l.fineStatus = 'UNPAID' AND l.memberId = :memberId ORDER BY l.returnDate DESC")
    List<FineDetailsDto> findUnpaidFineDetailsByMemberId(@Param("memberId") Integer memberId);
    
    List<Loan> findByStatusAndDueDate(LoanStatus status, LocalDate dueDate);
    
    @Query(value = "SELECT DATE_FORMAT(l.loan_date, '%Y-%m') AS month, COUNT(l.id) AS count " +
                   "FROM loans l " +
                   "WHERE l.loan_date BETWEEN :start AND :end " +
                   "GROUP BY month " +
                   "ORDER BY month ASC", nativeQuery = true)
    List<Map<String, Object>> findLoanCountsByMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query(value = "SELECT b.name AS bookName, COUNT(l.id) AS loanCount " +
                   "FROM loans l JOIN books b ON l.book_id = b.id " +
                   "WHERE l.loan_date BETWEEN :start AND :end " +
                   "GROUP BY b.name " +
                   "ORDER BY loanCount DESC " +
                   "LIMIT 5", nativeQuery = true)
    List<Map<String, Object>> findMostLoanedBooksInPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
