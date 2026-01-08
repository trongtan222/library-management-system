package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.UserChallengeProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserChallengeProgressRepository extends JpaRepository<UserChallengeProgress, Long> {

    List<UserChallengeProgress> findByUserId(Integer userId);
    
    Optional<UserChallengeProgress> findByUserIdAndChallengeId(Integer userId, Long challengeId);
    
    @Query("SELECT p FROM UserChallengeProgress p WHERE p.userId = :userId AND p.isCompleted = false")
    List<UserChallengeProgress> findActiveProgressByUser(@Param("userId") Integer userId);
    
    @Query("SELECT p FROM UserChallengeProgress p WHERE p.challenge.id = :challengeId AND p.isCompleted = true ORDER BY p.completedAt ASC")
    List<UserChallengeProgress> findCompletedByChallenge(@Param("challengeId") Long challengeId);
    
    @Query("SELECT COUNT(p) FROM UserChallengeProgress p WHERE p.challenge.id = :challengeId")
    long countParticipants(@Param("challengeId") Long challengeId);
}
