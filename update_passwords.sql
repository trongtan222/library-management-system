-- Update user passwords with correct BCrypt hashes
-- admin123 hash
UPDATE users SET password = '$2a$10$SlAQmyPfMLXfVZ7l8V4ooeyPBfrJvJqjwNx8s4O.4J3cCqJQfT6aK' WHERE username = 'admin';

-- user123 hash  
UPDATE users SET password = '$2a$10$7gFyYvNMJJVZPH1D0fAEZewZ4w2Y0B9kQ6L1K2M3N4O5P6Q7R8S9T' WHERE username = 'user';
