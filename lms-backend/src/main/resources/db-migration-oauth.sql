-- Migration script for Google OAuth support
ALTER TABLE users ADD COLUMN google_id VARCHAR(255) NULL UNIQUE;
ALTER TABLE users ADD COLUMN avatar VARCHAR(500) NULL;

CREATE INDEX idx_users_google_id ON users(google_id);
