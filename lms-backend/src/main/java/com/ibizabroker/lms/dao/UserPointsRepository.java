package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.UserPoints;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPointsRepository extends JpaRepository<UserPoints, Long> {

    Optional<UserPoints> findByUserId(Integer userId);
    
    @Query("SELECT u FROM UserPoints u ORDER BY u.totalPoints DESC")
    List<UserPoints> findTopUsers(Pageable pageable);
    
    @Query("SELECT u FROM UserPoints u ORDER BY u.currentLevel DESC, u.totalPoints DESC")
    List<UserPoints> findLeaderboard(Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM UserPoints u WHERE u.totalPoints > (SELECT up.totalPoints FROM UserPoints up WHERE up.userId = :userId)")
    long countUsersWithMorePoints(Integer userId);
}
