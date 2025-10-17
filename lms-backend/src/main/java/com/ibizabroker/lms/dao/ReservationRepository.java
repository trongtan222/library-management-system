package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Reservation;
import com.ibizabroker.lms.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    
    List<Reservation> findTop10ByBookIdAndStatusOrderByCreatedAtAsc(Integer bookId, ReservationStatus status);
    
    boolean existsByBookIdAndMemberIdAndStatus(Integer bookId, Integer memberId, ReservationStatus status);

    boolean existsByBookIdAndStatusAndMemberIdNot(Integer bookId, ReservationStatus status, Integer memberId);

    // === BỔ SUNG PHƯƠNG THỨC CÒN THIẾU TẠI ĐÂY ===
    List<Reservation> findByMemberIdAndStatus(Integer memberId, ReservationStatus status);
}