package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findByUserId(Integer userId);
    
    List<UserBadge> findByUserIdAndIsDisplayedTrue(Integer userId);
    
    @Query("SELECT ub FROM UserBadge ub WHERE ub.userId = :userId AND ub.badge.code = :badgeCode")
    Optional<UserBadge> findByUserIdAndBadgeCode(@Param("userId") Integer userId, @Param("badgeCode") String badgeCode);
    
    boolean existsByUserIdAndBadgeId(Integer userId, Long badgeId);
    
    @Query("SELECT COUNT(ub) FROM UserBadge ub WHERE ub.userId = :userId")
    long countByUserId(@Param("userId") Integer userId);
}
