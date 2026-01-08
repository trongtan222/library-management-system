package com.ibizabroker.lms.service;

import com.ibizabroker.lms.dao.*;
import com.ibizabroker.lms.dto.GamificationStatsDto;
import com.ibizabroker.lms.dto.LeaderboardEntryDto;
import com.ibizabroker.lms.entity.*;
import com.ibizabroker.lms.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Feature 9: Gamification Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Mặc định tất cả là chỉ đọc để tối ưu
public class GamificationService {

    private final UserPointsRepository pointsRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final ReadingChallengeRepository challengeRepository;
    private final UserChallengeProgressRepository progressRepository;
    private final UsersRepository usersRepository;

    // ============ POINTS ============
    
    // FIX: Đổi thành Read-Write vì hàm này sẽ gọi initializeUserPoints (INSERT) nếu chưa có dữ liệu
    @Transactional 
    public UserPoints getUserPoints(Integer userId) {
        return pointsRepository.findByUserId(userId)
                .orElseGet(() -> initializeUserPoints(userId));
    }

    // FIX: Thêm Transactional để cho phép Insert
    @Transactional
    public UserPoints initializeUserPoints(Integer userId) {
        UserPoints points = new UserPoints();
        points.setUserId(userId);
        points.setTotalPoints(0);
        points.setCurrentLevel(1);
        return pointsRepository.save(points); // Ghi xuống DB
    }

    // FIX: Thêm Transactional để cho phép Update
    @Transactional
    public void awardPoints(Integer userId, int points, String reason) {
        UserPoints userPoints = pointsRepository.findByUserId(userId)
                .orElseGet(() -> initializeUserPoints(userId));
        userPoints.addPoints(points);
        userPoints.setLastActivityDate(LocalDateTime.now());
        pointsRepository.save(userPoints);
        
        // Check for badges
        checkAndAwardBadges(userId, userPoints);
    }

    @Transactional
    public void onBookBorrowed(Integer userId) {
        UserPoints userPoints = getUserPoints(userId);
        userPoints.setBooksBorrowedCount(userPoints.getBooksBorrowedCount() + 1);
        awardPoints(userId, 10, "Mượn sách"); // +10 điểm khi mượn
        
        // Update challenge progress
        updateChallengeProgress(userId);
    }

    @Transactional
    public void onBookReturnedOnTime(Integer userId) {
        UserPoints userPoints = getUserPoints(userId);
        userPoints.setBooksReturnedOnTime(userPoints.getBooksReturnedOnTime() + 1);
        awardPoints(userId, 15, "Trả sách đúng hạn"); // +15 điểm khi trả đúng hạn
    }

    @Transactional
    public void onReviewWritten(Integer userId) {
        UserPoints userPoints = getUserPoints(userId);
        userPoints.setReviewsWritten(userPoints.getReviewsWritten() + 1);
        awardPoints(userId, 20, "Viết đánh giá"); // +20 điểm khi viết review
    }

    // ============ BADGES ============

    @Transactional(readOnly = true)
    public List<Badge> getAllBadges() {
        return badgeRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<UserBadge> getUserBadges(Integer userId) {
        return userBadgeRepository.findByUserId(userId);
    }

    // FIX: Hàm này gọi awardBadge (GHI) nên cần Transactional
    @Transactional
    public void checkAndAwardBadges(Integer userId, UserPoints userPoints) {
        List<Badge> allBadges = badgeRepository.findByIsActiveTrue();
        
        for (Badge badge : allBadges) {
            if (userBadgeRepository.existsByUserIdAndBadgeId(userId, badge.getId())) {
                continue; // Already has this badge
            }
            
            boolean earned = checkBadgeEligibility(badge, userPoints);
            if (earned) {
                awardBadge(userId, badge);
            }
        }
    }

    private boolean checkBadgeEligibility(Badge badge, UserPoints userPoints) {
        if (badge.getRequirementValue() == null) return false;
        
        return switch (badge.getCode()) {
            case "FIRST_BORROW" -> userPoints.getBooksBorrowedCount() >= 1;
            case "BOOKWORM_10" -> userPoints.getBooksBorrowedCount() >= 10;
            case "BOOKWORM_50" -> userPoints.getBooksBorrowedCount() >= 50;
            case "BOOKWORM_100" -> userPoints.getBooksBorrowedCount() >= 100;
            case "PUNCTUAL_5" -> userPoints.getBooksReturnedOnTime() >= 5;
            case "PUNCTUAL_20" -> userPoints.getBooksReturnedOnTime() >= 20;
            case "REVIEWER_5" -> userPoints.getReviewsWritten() >= 5;
            case "REVIEWER_20" -> userPoints.getReviewsWritten() >= 20;
            case "LEVEL_5" -> userPoints.getCurrentLevel() >= 5;
            case "STREAK_7" -> userPoints.getStreakDays() >= 7;
            case "STREAK_30" -> userPoints.getStreakDays() >= 30;
            default -> false;
        };
    }

    @Transactional
    public void awardBadge(Integer userId, Badge badge) {
        if (userBadgeRepository.existsByUserIdAndBadgeId(userId, badge.getId())) {
            return; // Already awarded
        }
        
        UserBadge userBadge = new UserBadge();
        userBadge.setUserId(userId);
        userBadge.setBadge(badge);
        userBadge.setEarnedAt(LocalDateTime.now());
        userBadgeRepository.save(userBadge);
        
        // Award bonus points for earning badge
        if (badge.getPointsReward() > 0) {
            awardPoints(userId, badge.getPointsReward(), "Đạt huy hiệu: " + badge.getNameVi());
        }
    }

    // ============ CHALLENGES ============

    @Transactional(readOnly = true)
    public List<ReadingChallenge> getActiveChallenges() {
        return challengeRepository.findActiveChallenges(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<UserChallengeProgress> getUserChallenges(Integer userId) {
        return progressRepository.findByUserId(userId);
    }

    @Transactional
    public UserChallengeProgress joinChallenge(Integer userId, Long challengeId) {
        @SuppressWarnings("null")
        ReadingChallenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thử thách với ID: " + challengeId));
        
        // Check if already joined
        if (progressRepository.findByUserIdAndChallengeId(userId, challengeId).isPresent()) {
            throw new IllegalStateException("Bạn đã tham gia thử thách này rồi.");
        }
        
        UserChallengeProgress progress = new UserChallengeProgress();
        progress.setUserId(userId);
        progress.setChallenge(challenge);
        progress.setBooksCompleted(0);
        progress.setIsCompleted(false);
        progress.setJoinedAt(LocalDateTime.now());
        
        return progressRepository.save(progress);
    }

    @Transactional
    public void updateChallengeProgress(Integer userId) {
        List<UserChallengeProgress> activeProgress = progressRepository.findActiveProgressByUser(userId);
        
        for (UserChallengeProgress progress : activeProgress) {
            progress.incrementProgress();
            
            if (progress.getIsCompleted()) {
                // Award challenge completion bonus
                ReadingChallenge challenge = progress.getChallenge();
                awardPoints(userId, challenge.getPointsReward(), "Hoàn thành thử thách: " + challenge.getNameVi());
                
                // Award badge if specified
                if (challenge.getBadgeId() != null) {
                    @SuppressWarnings("null")
                    Badge badge = badgeRepository.findById(challenge.getBadgeId()).orElse(null);
                    if (badge != null) {
                        awardBadge(userId, badge);
                    }
                }
            }
            
            progressRepository.save(progress);
        }
    }

    // ============ LEADERBOARD ============

    @Transactional(readOnly = true)
    public List<LeaderboardEntryDto> getLeaderboard(int limit) {
        List<UserPoints> topUsers = pointsRepository.findLeaderboard(PageRequest.of(0, limit));
        
        return topUsers.stream()
                .map(up -> {
                    @SuppressWarnings("null")
                    String userName = usersRepository.findById(up.getUserId())
                            .map(u -> u.getName())
                            .orElse("Unknown");
                    long badgeCount = userBadgeRepository.countByUserId(up.getUserId());
                    
                    return new LeaderboardEntryDto(
                            up.getUserId(),
                            userName,
                            up.getTotalPoints(),
                            up.getCurrentLevel(),
                            (int) badgeCount
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getUserRank(Integer userId) {
        return pointsRepository.countUsersWithMorePoints(userId) + 1;
    }

    // FIX: Đổi thành Read-Write vì logic này gọi getUserPoints -> có thể trigger initializeUserPoints (Save)
    @Transactional
    public GamificationStatsDto getUserStats(Integer userId) {
        UserPoints points = getUserPoints(userId);
        List<UserBadge> badges = getUserBadges(userId);
        List<UserChallengeProgress> challenges = getUserChallenges(userId);
        long rank = getUserRank(userId);
        
        return new GamificationStatsDto(
                points.getTotalPoints(),
                points.getCurrentLevel(),
                (int) rank,
                badges.size(),
                points.getBooksBorrowedCount(),
                points.getBooksReturnedOnTime(),
                points.getReviewsWritten(),
                points.getStreakDays(),
                challenges.stream().filter(c -> !c.getIsCompleted()).count(),
                challenges.stream().filter(UserChallengeProgress::getIsCompleted).count()
        );
    }
}