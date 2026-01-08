package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.ReadingChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReadingChallengeRepository extends JpaRepository<ReadingChallenge, Long> {

    List<ReadingChallenge> findByIsActiveTrue();
    
    @Query("SELECT c FROM ReadingChallenge c WHERE c.isActive = true AND c.startDate <= :now AND c.endDate >= :now")
    List<ReadingChallenge> findActiveChallenges(@Param("now") LocalDate now);
    
    @Query("SELECT c FROM ReadingChallenge c WHERE c.endDate < :now")
    List<ReadingChallenge> findExpiredChallenges(@Param("now") LocalDate now);
}
