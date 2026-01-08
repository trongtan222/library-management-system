-- ===============================================
-- DATABASE MIGRATION SCRIPT
-- Features: Location Tracking, E-books, Gamification
-- ===============================================

-- Feature 4: Book Locations
CREATE TABLE IF NOT EXISTS book_locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    floor INT NOT NULL DEFAULT 1,
    zone VARCHAR(50),
    shelf_code VARCHAR(50) NOT NULL,
    row_number INT NOT NULL DEFAULT 1,
    position INT NOT NULL DEFAULT 1,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    UNIQUE KEY uk_book_location (book_id)
);

-- Feature 5: E-books
CREATE TABLE IF NOT EXISTS ebooks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    description TEXT,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(20) NOT NULL DEFAULT 'PDF',
    file_size BIGINT DEFAULT 0,
    cover_url VARCHAR(500),
    is_public BOOLEAN DEFAULT TRUE,
    download_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    max_downloads_per_user INT DEFAULT 3,
    related_book_id INT,
    uploaded_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (related_book_id) REFERENCES books(id) ON DELETE SET NULL,
    FOREIGN KEY (uploaded_by) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS ebook_downloads (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ebook_id BIGINT NOT NULL,
    user_id INT NOT NULL,
    downloaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    FOREIGN KEY (ebook_id) REFERENCES ebooks(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Feature 9: Gamification - Points
CREATE TABLE IF NOT EXISTS user_points (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    total_points INT DEFAULT 0,
    current_level INT DEFAULT 1,
    books_borrowed_count INT DEFAULT 0,
    books_returned_on_time INT DEFAULT 0,
    reviews_written INT DEFAULT 0,
    streak_days INT DEFAULT 0,
    last_activity_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Feature 9: Gamification - Badges
CREATE TABLE IF NOT EXISTS badges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name_vi VARCHAR(100) NOT NULL,
    name_en VARCHAR(100) NOT NULL,
    description_vi TEXT,
    description_en TEXT,
    icon_url VARCHAR(255),
    category VARCHAR(50) DEFAULT 'GENERAL',
    requirement_type VARCHAR(50),
    requirement_value INT,
    points_reward INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_badges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    badge_id BIGINT NOT NULL,
    earned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (badge_id) REFERENCES badges(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_badge (user_id, badge_id)
);

-- Feature 9: Gamification - Challenges
CREATE TABLE IF NOT EXISTS reading_challenges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_vi VARCHAR(100) NOT NULL,
    name_en VARCHAR(100) NOT NULL,
    description_vi TEXT,
    description_en TEXT,
    target_books INT NOT NULL DEFAULT 5,
    points_reward INT DEFAULT 100,
    badge_id BIGINT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (badge_id) REFERENCES badges(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS user_challenge_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    challenge_id BIGINT NOT NULL,
    books_completed INT DEFAULT 0,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (challenge_id) REFERENCES reading_challenges(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_challenge (user_id, challenge_id)
);

-- ===============================================
-- SAMPLE DATA
-- ===============================================

-- Sample Badges
INSERT INTO badges (code, name_vi, name_en, description_vi, description_en, category, requirement_type, requirement_value, points_reward) VALUES
('FIRST_BORROW', 'Độc giả mới', 'New Reader', 'Mượn cuốn sách đầu tiên', 'Borrow your first book', 'BORROWING', 'BOOKS_BORROWED', 1, 10),
('BOOKWORM_10', 'Mọt sách', 'Bookworm', 'Mượn 10 cuốn sách', 'Borrow 10 books', 'BORROWING', 'BOOKS_BORROWED', 10, 50),
('BOOKWORM_50', 'Đại mọt sách', 'Super Bookworm', 'Mượn 50 cuốn sách', 'Borrow 50 books', 'BORROWING', 'BOOKS_BORROWED', 50, 200),
('BOOKWORM_100', 'Huyền thoại đọc sách', 'Legendary Reader', 'Mượn 100 cuốn sách', 'Borrow 100 books', 'BORROWING', 'BOOKS_BORROWED', 100, 500),
('PUNCTUAL_5', 'Đúng hạn', 'Punctual', 'Trả sách đúng hạn 5 lần', 'Return books on time 5 times', 'RETURNING', 'ON_TIME_RETURNS', 5, 25),
('PUNCTUAL_20', 'Siêu đúng hạn', 'Super Punctual', 'Trả sách đúng hạn 20 lần', 'Return books on time 20 times', 'RETURNING', 'ON_TIME_RETURNS', 20, 100),
('REVIEWER_5', 'Nhà phê bình', 'Critic', 'Viết 5 đánh giá', 'Write 5 reviews', 'REVIEWING', 'REVIEWS_WRITTEN', 5, 30),
('REVIEWER_20', 'Nhà phê bình cao cấp', 'Master Critic', 'Viết 20 đánh giá', 'Write 20 reviews', 'REVIEWING', 'REVIEWS_WRITTEN', 20, 150),
('LEVEL_5', 'Cấp độ 5', 'Level 5', 'Đạt cấp độ 5', 'Reach level 5', 'LEVEL', 'LEVEL_REACHED', 5, 75),
('STREAK_7', '7 ngày liên tiếp', '7 Day Streak', 'Hoạt động 7 ngày liên tiếp', '7 consecutive activity days', 'STREAK', 'STREAK_DAYS', 7, 40),
('STREAK_30', '30 ngày liên tiếp', '30 Day Streak', 'Hoạt động 30 ngày liên tiếp', '30 consecutive activity days', 'STREAK', 'STREAK_DAYS', 30, 200)
ON DUPLICATE KEY UPDATE name_vi = VALUES(name_vi);

-- Sample Reading Challenge
INSERT INTO reading_challenges (name_vi, name_en, description_vi, description_en, target_books, points_reward, start_date, end_date, is_active) VALUES
('Thử thách đọc hè 2025', 'Summer Reading Challenge 2025', 'Đọc 5 cuốn sách trong mùa hè', 'Read 5 books during summer', 5, 100, '2025-06-01', '2025-08-31', TRUE),
('Marathon đọc sách', 'Book Marathon', 'Đọc 10 cuốn trong 3 tháng', 'Read 10 books in 3 months', 10, 250, '2025-01-01', '2025-12-31', TRUE)
ON DUPLICATE KEY UPDATE name_vi = VALUES(name_vi);

-- Create indexes for performance
CREATE INDEX idx_ebook_downloads_user ON ebook_downloads(user_id);
CREATE INDEX idx_ebook_downloads_ebook ON ebook_downloads(ebook_id);
CREATE INDEX idx_user_points_total ON user_points(total_points DESC);
CREATE INDEX idx_book_locations_floor ON book_locations(floor);
CREATE INDEX idx_book_locations_shelf ON book_locations(shelf_code);
