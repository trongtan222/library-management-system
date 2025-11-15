-- DELETE existing test users to ensure CommandLineRunner creates them with correct hashes
-- Must delete user_role FIRST due to foreign key constraint, then users
DELETE FROM user_role WHERE user_id IN (SELECT user_id FROM users WHERE username IN ('admin', 'user'));
DELETE FROM users WHERE username IN ('admin', 'user');

-- Thêm vai trò
INSERT IGNORE INTO role (role_id, role_name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

-- NOTE: Test users (admin/admin123, user/user123) are now created by LibraryManagementApplication.java
-- This ensures passwords are correctly hashed with BCryptPasswordEncoder
-- REMOVED: Hardcoded user inserts with pre-hashed passwords

-- Thêm Tác Giả (Cập nhật)
INSERT IGNORE INTO authors (id, name) VALUES
(1, 'Harper Lee'), (2, 'George Orwell'), (3, 'F. Scott Fitzgerald'), (4, 'J.R.R. Tolkien'),
(5, 'Jane Austen'), (6, 'J.D. Salinger'), (7, 'Herman Melville'), (8, 'Leo Tolstoy'),
(9, 'Paulo Coelho'), (10, 'Yuval Noah Harari'), (11, 'Tara Westover'), (12, 'Michelle Obama'),
(13, 'Anne Frank'), (14, 'Bill Bryson'), (15, 'Barbara W. Tuchman'), (16, 'Frank Herbert'),
(17, 'Douglas Adams'), (18, 'J.K. Rowling'), (19, 'George R.R. Martin'), (20, 'Ray Bradbury'),
(21, 'Aldous Huxley'), (22, 'Patrick Rothfuss'), (23, 'Stephen R. Covey'), (24, 'Dale Carnegie'),
(25, 'Robert T. Kiyosaki'), (26, 'Daniel Kahneman'), (27, 'James Clear'), (28, 'Eric Ries'),
(29, 'Robert C. Martin'), 
(30, 'Andrew Hunt'), (31, 'Erich Gamma'), (32, 'Thomas H. Cormen'), (33, 'Stuart Russell'),
(34, 'Gayle Laakmann McDowell'), (35, 'Stieg Larsson'), (36, 'Gillian Flynn'), (37, 'Dan Brown'),
(38, 'Agatha Christie'), (39, 'Marcus Aurelius'), (40, 'Friedrich Nietzsche'), (41, 'Jostein Gaarder'),
(42, 'Vũ Trọng Phụng'), (43, 'Tô Hoài'), (44, 'Ngô Tất Tố'), (45, 'Nguyễn Nhật Ánh'),
(46, 'Đoàn Giỏi'),
-- Thêm các tác giả còn thiếu
(47, 'David Thomas'), -- Cho sách 'The Pragmatic Programmer'
(48, 'Richard Helm'), -- Cho sách 'Design Patterns'
(49, 'Ralph Johnson'), -- Cho sách 'Design Patterns'
(50, 'John Vlissides'); -- Cho sách 'Design Patterns'

-- Thêm Thể Loại
INSERT IGNORE INTO categories (id, name) VALUES
(1, 'Tiểu thuyết'), (2, 'Phản địa đàng'), (3, 'Giả tưởng'), (4, 'Lãng mạn'), (5, 'Phiêu lưu'),
(6, 'Tiểu thuyết lịch sử'), (7, 'Lịch sử'), (8, 'Hồi ký'), (9, 'Tiểu sử'), (10, 'Khoa học'),
(11, 'Khoa học viễn tưởng'), (12, 'Phát triển bản thân'), (13, 'Tài chính'), (14, 'Tâm lý học'),
(15, 'Kinh doanh'), (16, 'Công nghệ phần mềm'), (17, 'Khoa học máy tính'), (18, 'Trí tuệ nhân tạo'),
(19, 'Trinh thám'), (20, 'Bí ẩn'), (21, 'Triết học'), (22, 'Trào phúng'), (23, 'Thiếu nhi'),
(24, 'Hiện thực xã hội');

-- Thêm Sách
INSERT IGNORE INTO books (id, number_of_copies_available, published_year, isbn, cover_url, name) VALUES
(1, 4, 1960, '978-0061120084', 'http://books.google.com/books/content?id=3t5dtAEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api', 'To Kill a Mockingbird'),
(2, 7, 1949, '978-0451524935', NULL, '1984'),
(3, 4, 1925, '978-0743273565', NULL, 'The Great Gatsby'),
(4, 6, 1954, '978-0618640157', NULL, 'The Lord of the Rings'),
(5, 8, 1813, '978-1503290563', NULL, 'Pride and Prejudice'),
(6, 3, 1951, '978-0316769488', NULL, 'The Catcher in the Rye'),
(7, 4, 1851, '978-1503280786', NULL, 'Moby Dick'),
(8, 1, 1869, '978-1420952138', NULL, 'War and Peace'),
(9, 15, 1988, '978-0061122416', NULL, 'The Alchemist'),
(10, 10, 2011, '978-0062316097', NULL, 'Sapiens: A Brief History of Humankind'),
(11, 6, 2018, '978-0399590504', NULL, 'Educated'),
(12, 8, 2018, '978-1524763138', NULL, 'Becoming'),
(13, 5, 1947, '978-0553296983', NULL, 'The Diary of a Young Girl'),
(14, 7, 2003, '978-0767908184', 'http://books.google.com/books/content?id=hhnIWGD5Zt0C&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 'A Short History of Nearly Everything'),
(15, 4, 1962, '978-0345386236', NULL, 'The Guns of August'),
(16, 6, 1965, '978-0441013593', NULL, 'Dune'),
(17, 9, 1979, '978-0345391803', NULL, 'The Hitchhiker''s Guide to the Galaxy'),
(18, 12, 1997, '978-0590353427', NULL, 'Harry Potter and the Sorcerer''s Stone'),
(19, 5, 1996, '978-0553593716', NULL, 'A Game of Thrones'),
(20, 6, 1953, '978-1451673319', NULL, 'Fahrenheit 451'),
(21, 5, 1932, '978-0060850524', NULL, 'Brave New World'),
(22, 8, 2007, '978-0756404741', NULL, 'The Name of the Wind'),
(23, 10, 1989, '978-1982137274', NULL, 'The 7 Habits of Highly Effective People'),
(24, 11, 1936, '978-0671027032', NULL, 'How to Win Friends and Influence People'),
(25, 8, 1997, '978-1612680194', NULL, 'Rich Dad Poor Dad'),
(26, 5, 2011, '978-0374533557', NULL, 'Thinking, Fast and Slow'),
(27, 15, 2018, '978-0735211292', NULL, 'Atomic Habits'),
(28, 9, 2011, '978-0307887894', NULL, 'The Lean Startup'),
(29, 7, 2008, '978-0132350884', NULL, 'Clean Code'),
(30, 6, 1999, '978-0201616224', NULL, 'The Pragmatic Programmer'),
(31, 4, 1994, '978-0201633610', NULL, 'Design Patterns'),
(32, 3, 1990, '978-0262033848', NULL, 'Introduction to Algorithms'),
(33, 4, 1995, '978-0136042594', NULL, 'Artificial Intelligence: A Modern Approach'),
(34, 10, 2015, '978-0984782857', NULL, 'Cracking the Coding Interview'),
(35, 7, 2005, '978-0307473479', NULL, 'The Girl with the Dragon Tattoo'),
(36, 8, 2012, '978-0307588371', NULL, 'Gone Girl'),
(37, 10, 2003, '978-0307474278', NULL, 'The Da Vinci Code'),
(38, 9, 1939, '978-0312330873', NULL, 'And Then There Were None'),
(39, 6, 180, '978-0140449334', NULL, 'Meditations'),
(40, 3, 1883, '978-0140441185', NULL, 'Thus Spoke Zarathustra'),
(41, 5, 1991, '978-0374530716', 'http://books.google.com/books/content?id=J8nE3B5lD9AC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api', 'Sophie''s World'),
(42, 7, 1936, '978-6049692408', NULL, 'Số Đỏ'),
(43, 15, 1941, '978-6042058315', NULL, 'Dế Mèn Phiêu Lưu Ký'),
(44, 6, 1937, '978-6049533367', NULL, 'Tắt Đèn'),
(45, 20, 2008, '978-6042188708', NULL, 'Cho Tôi Xin Một Vé Đi Tuổi Thơ'),
(46, 10, 1957, '978-6042083232', NULL, 'Đất Rừng Phương Nam');

-- Thêm quan hệ Sách - Tác giả (Cập nhật)
INSERT IGNORE INTO book_authors (book_id, author_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7), (8, 8), (9, 9), (10, 10),
(11, 11), (12, 12), (13, 13), (14, 14), (15, 15), (16, 16), (17, 17), (18, 18), (19, 19),
(20, 20), (21, 21), (22, 22), (23, 23), (24, 24), (25, 25), (26, 26), (27, 27), (28, 28),
(29, 29), 
-- Cập nhật 'The Pragmatic Programmer' (ID 30)
(30, 30), (30, 47),
-- Cập nhật 'Design Patterns' (ID 31) - "Gang of Four"
(31, 31), (31, 48), (31, 49), (31, 50),
(32, 32), (33, 33), (34, 34), (35, 35), (36, 36), (37, 37),
(38, 38), (39, 39), (40, 40), (41, 41), (42, 42), (43, 43), (44, 44), (45, 45), (46, 46);

-- Thêm quan hệ Sách - Thể loại (Cập nhật)
INSERT IGNORE INTO book_categories (book_id, category_id) VALUES
(1, 1), 
(2, 2), (2, 1), -- '1984' (Phản địa đàng, Tiểu thuyết)
(3, 1), 
(4, 3), 
(5, 4), 
(6, 1), 
(7, 5), 
(8, 6), (8, 1), -- 'War and Peace' (Tiểu thuyết lịch sử, Tiểu thuyết)
(9, 3), 
(10, 7),
(11, 8), 
(12, 8), 
(13, 9), 
(14, 10), 
(15, 7), 
(16, 11), (16, 1), -- 'Dune' (Khoa học viễn tưởng, Tiểu thuyết)
(17, 11), 
(18, 3), 
(19, 3),
(20, 2), (20, 1), -- 'Fahrenheit 451' (Phản địa đàng, Tiểu thuyết)
(21, 2), (21, 1), -- 'Brave New World' (Phản địa đàng, Tiểu thuyết)
(22, 3), 
(23, 12), 
(24, 12), 
(25, 13), 
(26, 14), 
(27, 12), 
(28, 15),
(29, 16), 
(30, 16), 
(31, 16), 
(32, 17), 
(33, 18), 
(34, 17), 
(35, 19), 
(36, 19), 
(37, 20), (37, 19), -- 'The Da Vinci Code' (Bí ẩn, Trinh thám)
(38, 20), 
(39, 21), 
(40, 21), 
(41, 21), (41, 1), -- 'Sophie's World' (Triết học, Tiểu thuyết)
(42, 22), (42, 1), -- 'Số Đỏ' (Trào phúng, Tiểu thuyết)
(43, 23), 
(44, 24), (44, 1), -- 'Tắt Đèn' (Hiện thực xã hội, Tiểu thuyết)
(45, 23), 
(46, 5);